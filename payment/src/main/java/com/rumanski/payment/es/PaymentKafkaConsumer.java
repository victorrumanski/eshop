package com.rumanski.payment.es;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rumanski.payment.SpringContext;
import com.rumanski.payment.es.events.PaymentCompletedEvent;
import com.rumanski.payment.es.events.PaymentDomainEvent;
import com.rumanski.payment.es.events.PaymentDomainEvent.EventType;
import com.rumanski.payment.es.events.PaymentFailedEvent;
import com.rumanski.payment.model.Payment;
import com.rumanski.payment.model.Payment.PaymentStatus;
import com.rumanski.payment.repository.PaymentRepository;

import io.zeebe.client.ZeebeClient;

@Component
/*
 * This class will only log messages from kafka
 */
public class PaymentKafkaConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentKafkaConsumer.class);

	@Value("${kafka.bootstrap.servers}")
	private String kafkaBootstrapServers;

	@Value("${zookeeper.groupid}")
	private String zookeeperGroupId;

	private KafkaConsumer<String, String> consumer;

	private static boolean FAIL_PAYMENTS = false;

	@PostConstruct
	void postConstruct() {

		Properties consumerProperties = new Properties();
		consumerProperties.put("bootstrap.servers", kafkaBootstrapServers);
		consumerProperties.put("group.id", zookeeperGroupId);
		consumerProperties.put("zookeeper.session.timeout.ms", "6000");
		consumerProperties.put("zookeeper.sync.time.ms", "2000");
		consumerProperties.put("auto.commit.enable", "false");
		consumerProperties.put("auto.commit.interval.ms", "1000");
		consumerProperties.put("consumer.timeout.ms", "-1");
		consumerProperties.put("max.poll.records", "1");
		consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		LOGGER.info("Starting Kafka Consumer");
		consumer = new KafkaConsumer<>(consumerProperties);

		/*
		 * Creating a thread to run the infinite loop
		 */
		Thread kafkaConsumerThread = new Thread(() -> {
			LOGGER.info("Starting Kafka consumer thread.");
			ConsumerLoop simpleKafkaConsumer = new ConsumerLoop(consumer);
			simpleKafkaConsumer.loop();
		});

		/*
		 * Starting the first thread.
		 */
		kafkaConsumerThread.start();
		LOGGER.info("Kafka Consumer Started");
	}

	@PreDestroy
	void preDestroy() {
		LOGGER.info("Closing Kafka Consumer");
		ConsumerLoop.stop = true;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		consumer.close();
		LOGGER.info("Kafka Consumer Closed");
	}

	private static class ConsumerLoop {

		public static boolean stop = false;

		private final KafkaConsumer<String, String> consumer;

		public ConsumerLoop(KafkaConsumer<String, String> consumer) {
			super();
			this.consumer = consumer;
		}

		public void loop() {

			ZeebeClient zeebeClient = SpringContext.getBean(ZeebeClient.class);
			EventStore store = SpringContext.getBean(EventStore.class);
			PaymentRepository paymentRepo = SpringContext.getBean(PaymentRepository.class);

			List<String> topics = new ArrayList<>();
			EventType[] values = PaymentDomainEvent.EventType.values();
			for (EventType et : values) {
				topics.add(et.name());
			}
			consumer.subscribe(topics);
			LOGGER.info("Kafka Consumer subscribed to topics");

			// here comes kafka loop

			/*
			 * We will start an infinite while loop, inside which we'll be listening to new
			 * messages in each topic that we've subscribed to.
			 */
			while (!stop) {

				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

				for (ConsumerRecord<String, String> record : records) {

					/*
					 * Whenever there's a new message in the Kafka topic, we'll get the message in
					 * this loop, as the record object.
					 */

					/*
					 * Getting the message as a string from the record object.
					 */
					String topic = record.topic();
					String key = record.key();
					String message = record.value();

					LOGGER.info("Received kafka message: " + message + " on topic " + topic + " with key " + key);
					if (topic.equals(PaymentDomainEvent.EventType.PAYMENT_REQUESTED.name())) {
						try {
							ObjectMapper reader = new ObjectMapper();
							Map<?, ?> event = reader.readValue(message, Map.class);
							// calls payment api
							LOGGER.info("Calling Onboarding Card id");
							final String uri = "http://localhost:8914/api/onboarding/cards/" + event.get("cardid");
							RestTemplate restTemplate = new RestTemplate();
							String result = restTemplate.getForObject(uri, String.class);
							Map card = reader.readValue(result, Map.class);
							LOGGER.info("Calling Third Party PAYMENT API with values " + result + " and amount "
									+ event.get("amount"));

							Long paymentid = Long.parseLong("" + key);
							Long orderid = Long.parseLong("" + event.get("orderid"));
							Long cardid = Long.parseLong("" + event.get("cardid"));
							BigDecimal amount = new BigDecimal("" + event.get("amount"));

							Map vars = new HashMap<>();
							vars.put("orderid", orderid);
							vars.put("paymentid", paymentid);

							if (FAIL_PAYMENTS) {
								String reason = "Because I want the payment to fail";
								vars.put("reason", reason);

								Payment payment = paymentRepo.findById(paymentid).get();
								payment.setStatus(PaymentStatus.PAYMENT_FAILED);
								payment.setFailed(new Date());
								paymentRepo.save(payment);

								PaymentFailedEvent internalEvent = new PaymentFailedEvent(orderid, cardid, amount,
										reason);
								store.saveAndPublish(internalEvent.toEventTable(paymentid));

								zeebeClient
										.newPublishMessageCommand()
										.messageName("payment-failed")
										.correlationKey(orderid+"")
										.variables(vars)
										.send()
										.join();
							} else {
								Payment payment = paymentRepo.findById(paymentid).get();
								payment.setStatus(PaymentStatus.PAYMENT_COMPLETED);
								payment.setPaid(new Date());
								paymentRepo.save(payment);

								PaymentCompletedEvent internalEvent = new PaymentCompletedEvent(orderid, cardid,
										amount);
								store.saveAndPublish(internalEvent.toEventTable(paymentid));

								zeebeClient
										.newPublishMessageCommand()
										.messageName("payment-completed")
										.correlationKey(orderid+"")
										.variables(vars)
										.send()
										.join();
							}

						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}

					{
						Map<TopicPartition, OffsetAndMetadata> commitMessage = new HashMap<>();

						commitMessage.put(new TopicPartition(record.topic(), record.partition()),
								new OffsetAndMetadata(record.offset() + 1));

						consumer.commitSync(commitMessage);

						LOGGER.info("Offset committed to Kafka.");
					}
				}
			}
		}
	}

}
