package com.rumanski.catalog.es.store;

import java.time.Duration;
import java.util.ArrayList;
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

import com.rumanski.catalog.es.events.CatalogDomainEvent;
import com.rumanski.catalog.es.events.CatalogDomainEvent.EventType;

@Component
/*
 * This class will only log messages from kafka
 */
public class CatalogKafkaConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(CatalogKafkaConsumer.class);

	@Value("${kafka.bootstrap.servers}")
	private String kafkaBootstrapServers;

	@Value("${zookeeper.groupid}")
	private String zookeeperGroupId;

	private KafkaConsumer<String, String> consumer;

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
			List<String> topics = new ArrayList<>();
			EventType[] values = CatalogDomainEvent.EventType.values();
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

					/*
					 * Logging the received message to the console.
					 */
					LOGGER.info("Received kafka message: " + message + " on topic " + topic + " with key " + key);

					/*
					 * Once we finish processing a Kafka message, we have to commit the offset so
					 * that we don't end up consuming the same message endlessly. By default, the
					 * consumer object takes care of this. But to demonstrate how it can be done, we
					 * have turned this default behaviour off, instead, we're going to manually
					 * commit the offsets. The code for this is below. It's pretty much self
					 * explanatory.
					 */
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
