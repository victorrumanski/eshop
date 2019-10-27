package com.rumanski.basket.es.store;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.rumanski.basket.model.BasketEvent;

@Component
public class BasketKafkaProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(BasketKafkaProducer.class);

	@Value("${kafka.bootstrap.servers}")
	private String kafkaBootstrapServers;

	private KafkaProducer<String, String> producer;

	@PostConstruct
	void postConstruct() {
		Properties producerProperties = new Properties();
		producerProperties.put("bootstrap.servers", kafkaBootstrapServers);
		producerProperties.put("acks", "all");
		producerProperties.put("retries", 0);
		producerProperties.put("batch.size", 16384);
		producerProperties.put("linger.ms", 1);
		producerProperties.put("buffer.memory", 33554432);
		producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		producerProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		LOGGER.info("Starting Kafka Producer");
		producer = new KafkaProducer<>(producerProperties);
		LOGGER.info("Kafka Producer Started");
	}

	@PreDestroy
	void preDestroy() {
		LOGGER.info("Closing Kafka Producer");
		producer.close();
		LOGGER.info("Kafka Producer Closed");
	}

	/**
	 * Runs in a new thread after the current transaction has committed.
	 * 
	 * <br />
	 * This method MUST NOT run if the transaction was rollbacked.
	 * 
	 * <br />
	 * Sends the event to kafka broker, so that kafka consumers are notified of data
	 * changes from this MicroService.
	 */
	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void springEventListener(BasketEvent event) {
		LOGGER.info("Sending event to Kafka: " + event);
		sendKafkaMessage(event);
	}

	private void sendKafkaMessage(BasketEvent event) {
		String topic = event.type, value = event.payload;
		if (event.getCorrelationid() != null) {
			// send key
			producer.send(
					new ProducerRecord<String, String>(topic, event.getCorrelationid().toString(), value));
		} else {
			// send without key
			producer.send(new ProducerRecord<String, String>(topic, value));
		}
	}

}
