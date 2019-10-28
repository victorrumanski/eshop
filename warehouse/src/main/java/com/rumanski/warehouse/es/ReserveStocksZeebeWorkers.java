package com.rumanski.warehouse.es;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rumanski.warehouse.SpringContext;
import com.rumanski.warehouse.es.events.ReservationCompletedEvent;
import com.rumanski.warehouse.es.store.EventStore;
import com.rumanski.warehouse.model.Reservation;
import com.rumanski.warehouse.model.Reservation.ReservationStatus;
import com.rumanski.warehouse.model.Stock;
import com.rumanski.warehouse.repository.ReservationRepository;
import com.rumanski.warehouse.repository.StockRepository;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.client.api.worker.JobHandler;
import io.zeebe.client.api.worker.JobWorker;

@Component
@Transactional
public class ReserveStocksZeebeWorkers {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReserveStocksZeebeWorkers.class);

	@Autowired
	private ZeebeClient zeebeClient;

	private JobWorker reserverStocksWorker, reserverStocksCancelationWorker;

	@PostConstruct
	public void start() {
		reserverStocksWorker = zeebeClient
				.newWorker()
				.jobType("reserve-stocks")
				.handler(new ReserverStocks())
				.open();

		reserverStocksCancelationWorker = zeebeClient
				.newWorker()
				.jobType("reserve-stocks-cancellation")
				.handler(new ReserveStocksCancellation())
				.open();
	}

	@PreDestroy
	public void stop() {
		reserverStocksWorker.close();
		reserverStocksCancelationWorker.close();
	}

	public static class ReserverStocks implements JobHandler {

		private static final Logger log = LoggerFactory.getLogger(ReserverStocks.class);

		@Override
		public void handle(JobClient client, ActivatedJob job) throws Exception {
			ReservationRepository reservationRepo = SpringContext.getBean(ReservationRepository.class);
			StockRepository stockRepo = SpringContext.getBean(StockRepository.class);
			EventStore store = SpringContext.getBean(EventStore.class);

			logJob(job);

			Map<String, Object> vars = job.getVariablesAsMap();

			Map<String, Object> order = (Map<String, Object>) vars.get("order");
			List<Map<String, Object>> items = (List<Map<String, Object>>) order.get("items");
			for (Map<String, Object> item : items) {
				Long orderitemid = Long.parseLong("" + item.get("id"));
				Long productid = Long.parseLong("" + item.get("productid"));
				BigDecimal quantity = new BigDecimal("" + item.get("quantity"));

				Reservation r = new Reservation();
				r.setOrderitemid(orderitemid);
				r.setQuantity(quantity);
				r.setStatus(ReservationStatus.RESERVED);
				Stock stock = stockRepo.findByProductid(productid).get(0);
				r.setStockid(stock.getId());
				r = reservationRepo.save(r);

				stock.setReserved(stock.getReserved().add(r.getQuantity()));
				stock.setAvailable(stock.getTotal().subtract(stock.getReserved()));
				stock = stockRepo.save(stock);

				item.put("reservation", r);

				ReservationCompletedEvent internalEvent = new ReservationCompletedEvent(orderitemid, stock.getId(),
						quantity);
				store.saveAndPublish(internalEvent.toEventTable(stock.getId()));
			}

			reservationRepo.flush();
			stockRepo.flush();

			
			// ********* //
			// ********* //
			// ********* //
			
			vars.put("success", true);
			
			// ********* //
			// ********* //
			// ********* //
			
			
			ObjectMapper mapper = new ObjectMapper();

			log.info("vars after: " + mapper.writeValueAsString(vars));
			client.newCompleteCommand(job.getKey()).variables(vars).send().join();
			log.info(" step completed");
		}
	}

	public static class ReserveStocksCancellation implements JobHandler {
		private static final Logger log = LoggerFactory.getLogger(ReserveStocksCancellation.class);

		@Override
		public void handle(JobClient client, ActivatedJob job) throws Exception {
			logJob(job);
			Map<String, Object> vars = job.getVariablesAsMap();
			log.info(" vars: " + job.getVariables());
			ReservationRepository reservationRepo = SpringContext.getBean(ReservationRepository.class);
			Map<String, Object> order = (Map<String, Object>) vars.get("order");
			List<Map<String, Object>> items = (List<Map<String, Object>>) order.get("items");
			for (Map<String, Object> item : items) {
				Map reservation = (Map) item.get("reservation");
				Reservation res = reservationRepo.findById(Long.parseLong("" + reservation.get("id"))).get();
				res.setStatus(ReservationStatus.CANCELED);
				reservationRepo.save(res);
			}
			reservationRepo.flush();
			client.newCompleteCommand(job.getKey()).variables(vars).send().join();
			log.info(" step completed");
		}
	}

	private static void logJob(final ActivatedJob job) {
		LOGGER.info(
				"complete job\n>>> [type: {}, key: {}, element: {}, workflow instance: {}]\n{deadline; {}]\n[headers: {}]\n[variables: {}]",
				job.getType(),
				job.getKey(),
				job.getElementId(),
				job.getWorkflowInstanceKey(),
				Instant.ofEpochMilli(job.getDeadline()),
				job.getCustomHeaders(),
				job.getVariables());
	}

}
