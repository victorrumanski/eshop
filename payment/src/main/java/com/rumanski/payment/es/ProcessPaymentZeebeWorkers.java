package com.rumanski.payment.es;

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
import com.rumanski.payment.SpringContext;
import com.rumanski.payment.es.events.PaymentRefundedEvent;
import com.rumanski.payment.es.events.PaymentRequestedEvent;
import com.rumanski.payment.model.Payment;
import com.rumanski.payment.model.Payment.PaymentStatus;
import com.rumanski.payment.model.PaymentEvent;
import com.rumanski.payment.model.Refund;
import com.rumanski.payment.repository.PaymentRepository;
import com.rumanski.payment.repository.RefundRepository;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.client.api.worker.JobHandler;
import io.zeebe.client.api.worker.JobWorker;

@Component
@Transactional
public class ProcessPaymentZeebeWorkers {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessPaymentZeebeWorkers.class);

	@Autowired
	private ZeebeClient zeebeClient;

	private JobWorker processPaymentWorker, processPaymentCancelationWorker;

	@PostConstruct
	public void start() {
		processPaymentWorker = zeebeClient.newWorker().jobType("process-payment").handler(new ProcessPayment()).open();

		processPaymentCancelationWorker = zeebeClient.newWorker().jobType("process-payment-cancellation")
				.handler(new ProcessPaymentCancellation()).open();
	}

	@PreDestroy
	public void stop() {
		processPaymentWorker.close();
		processPaymentCancelationWorker.close();
	}

	public static class ProcessPayment implements JobHandler {

		private static final Logger log = LoggerFactory.getLogger(ProcessPayment.class);

		@Override
		public void handle(JobClient client, ActivatedJob job) throws Exception {
			PaymentRepository paymentRepo = SpringContext.getBean(PaymentRepository.class);
			EventStore store = SpringContext.getBean(EventStore.class);
			PaymentKafkaProducer paymentKafkaProducer = SpringContext.getBean(PaymentKafkaProducer.class);

			logJob(job);

			Map<String, Object> vars = job.getVariablesAsMap();

			Map<String, Object> order = (Map<String, Object>) vars.get("order");
			List<Map<String, Object>> items = (List<Map<String, Object>>) order.get("items");
			BigDecimal total = BigDecimal.ZERO;
			for (Map<String, Object> item : items) {
				BigDecimal quantity = new BigDecimal("" + item.get("quantity"));
				BigDecimal price = new BigDecimal("" + item.get("price"));
				total = total.add(quantity.multiply(price));
			}

			Long orderid = Long.parseLong("" + order.get("id"));
			Long cardid = Long.parseLong("" + order.get("cardid"));

			Payment payment = new Payment();
			payment.setOrderid(orderid);
			payment.setCardid(cardid);
			payment.setAmount(total);
			payment.setStatus(PaymentStatus.PAYMENT_REQUESTED);

			payment = paymentRepo.save(payment);
			vars.put("payment", payment);

			PaymentRequestedEvent internalEvent = new PaymentRequestedEvent(orderid, cardid, total);
			PaymentEvent go = internalEvent.toEventTable(payment.getId());
			store.saveAndPublish(go);
			paymentRepo.flush();

			vars.put("orderid", orderid);
			ObjectMapper mapper = new ObjectMapper();
			log.info("vars after: " + mapper.writeValueAsString(vars));
			client.newCompleteCommand(job.getKey()).variables(vars).send().join();

			paymentKafkaProducer.sendKafkaMessage(go);
			log.info(" step completed");
		}
	}

	public static class ProcessPaymentCancellation implements JobHandler {
		private static final Logger log = LoggerFactory.getLogger(ProcessPaymentCancellation.class);

		@Override
		public void handle(JobClient client, ActivatedJob job) throws Exception {
			logJob(job);

			RefundRepository refundRepo = SpringContext.getBean(RefundRepository.class);
			PaymentRepository paymentRepo = SpringContext.getBean(PaymentRepository.class);
			EventStore store = SpringContext.getBean(EventStore.class);

			Map<String, Object> vars = job.getVariablesAsMap();
			log.info(" vars: " + job.getVariables());
			Long paymentid = Long.parseLong("" + vars.get("paymentid"));
//			Long orderid = Long.parseLong("" + vars.get("orderid"));
//			Long cardid = Long.parseLong("" + vars.get("cardid"));
//			BigDecimal amount = new BigDecimal("" + ((Map) vars.get("payment")).get("amount"));

			Payment payment = paymentRepo.findById(paymentid).get();

			if (payment.getStatus() == PaymentStatus.PAYMENT_COMPLETED) {
				// we need to make a refund
				Refund refund = new Refund();
				refund.setPaymentid(paymentid);
				refundRepo.save(refund);

				PaymentRefundedEvent internalEvent = new PaymentRefundedEvent(paymentid);
				store.saveAndPublish(internalEvent.toEventTable(payment.getId()));
			} else if (payment.getStatus() == PaymentStatus.PAYMENT_FAILED) {
				// we only need to bypass, because FAILED=CANCELED
			} else {
				throw new RuntimeException("Payment " + paymentid + " is in status = " + payment.getStatus()
						+ " I dont know how to cancel it.");
				// zeebe will stop at this step because it found an error
			}

			paymentRepo.flush();
			client.newCompleteCommand(job.getKey()).variables(vars).send().join();
			log.info(" step completed");
		}
	}

	private static void logJob(final ActivatedJob job) {
		LOGGER.info(
				"complete job\n>>> [type: {}, key: {}, element: {}, workflow instance: {}]\n{deadline; {}]\n[headers: {}]\n[variables: {}]",
				job.getType(), job.getKey(), job.getElementId(), job.getWorkflowInstanceKey(),
				Instant.ofEpochMilli(job.getDeadline()), job.getCustomHeaders(), job.getVariables());
	}

}
