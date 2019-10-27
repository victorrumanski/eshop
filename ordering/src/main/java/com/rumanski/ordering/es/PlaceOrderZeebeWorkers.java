package com.rumanski.ordering.es;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.rumanski.ordering.SpringContext;
import com.rumanski.ordering.es.events.OrderCreatedEvent;
import com.rumanski.ordering.es.store.EventStore;
import com.rumanski.ordering.model.Order;
import com.rumanski.ordering.model.Order.OrderStatus;
import com.rumanski.ordering.model.OrderItem;
import com.rumanski.ordering.repository.OrderItemRepository;
import com.rumanski.ordering.repository.OrderRepository;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.client.api.worker.JobHandler;
import io.zeebe.client.api.worker.JobWorker;

@Component
@Transactional
public class PlaceOrderZeebeWorkers {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlaceOrderZeebeWorkers.class);

//	@Autowired
//	OrderItemRepository orderItemRepo;
//
//	@Autowired
//	OrderRepository orderRepo;
//
//	@Autowired
//	EventStore store;

	@Autowired
	private ZeebeClient zeebeClient;

	private JobWorker createOrderWorker, createOrderCancelationWorker;

	@PostConstruct
	public void start() {
		createOrderWorker = zeebeClient
				.newWorker()
				.jobType("create-order")
				.handler(new CreateOrder())
				.open();

		createOrderCancelationWorker = zeebeClient
				.newWorker()
				.jobType("create-order-cancelation")
				.handler(new CreateOrderCancelation())
				.open();
	}

	@PreDestroy
	public void stop() {
		createOrderWorker.close();
		createOrderCancelationWorker.close();
	}

//	@ZeebeWorker(type = "create-order")
	public static class CreateOrder implements JobHandler {
		private static final Logger log = LoggerFactory.getLogger(PlaceOrderZeebeWorkers.class);

		@Override
		public void handle(JobClient client, ActivatedJob job) throws Exception {
			OrderItemRepository orderItemRepo = SpringContext.getBean(OrderItemRepository.class);
			OrderRepository orderRepo = SpringContext.getBean(OrderRepository.class);
			EventStore store = SpringContext.getBean(EventStore.class);

			logJob(job);
			Map<String, Object> vars = job.getVariablesAsMap();
			log.info("vars: " + vars);

			Long userid = Long.parseLong(vars.get("userid").toString());
			Long addressid = Long.parseLong(vars.get("addressid").toString());
			Long cardid = Long.parseLong(vars.get("cardid").toString());

			Order order = new Order();
			order.setCreated(new Date());
			order.setUserid(userid);
			order.setAddressid(addressid);
			order.setCardid(cardid);
			order.setStatus(OrderStatus.CREATED);
			order = orderRepo.save(order);
			Long orderid = order.getId();

			List<OrderItem> orderItems = new ArrayList<>();

			List<Map> basketItems = (List) vars.get("items");
			basketItems.forEach(item -> {
				OrderItem orderitem = new OrderItem();
				orderitem.setOrderid(orderid);
				orderitem.setProductid(Long.parseLong("" + item.get("productid")));
				orderitem.setPrice(new BigDecimal("" + item.get("price")));
				orderitem.setQuantity(new BigDecimal("" + item.get("quantity")));
				orderItemRepo.save(orderitem);
				orderItems.add(orderitem);
			});
			order.setItems(orderItems);

			OrderCreatedEvent internalEvent = new OrderCreatedEvent(order);
			store.saveAndPublish(internalEvent.toEventTable(order.getId()));

			orderRepo.flush();
			orderItemRepo.flush();

			vars.put("order", order);
			client.newCompleteCommand(job.getKey()).variables(vars).send().join();
			log.info("step completed");
		}
	}

//	@ZeebeWorker(type = "create-order-cancelation")
	public static class CreateOrderCancelation implements JobHandler {
		private static final Logger log = LoggerFactory.getLogger(PlaceOrderZeebeWorkers.class);

		@Override
		public void handle(JobClient client, ActivatedJob job) throws Exception {
			logJob(job);
			Map<String, Object> vars = job.getVariablesAsMap();
			log.info("vars: " + job.getVariables());
			Map order = (Map) vars.get("order");
			Object orderid = order.get("id");
			OrderRepository orderRepo = SpringContext.getBean(OrderRepository.class);
			Order o = orderRepo.findById(Long.parseLong(orderid.toString())).get();
			o.setStatus(OrderStatus.CANCELED);
			orderRepo.save(o);
			orderRepo.flush();
			client.newCompleteCommand(job.getKey()).variables(vars).send().join();
			log.info("step completed");
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
