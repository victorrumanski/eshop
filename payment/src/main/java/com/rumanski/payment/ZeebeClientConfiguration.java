package com.rumanski.payment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.zeebe.client.ZeebeClient;

@Configuration
public class ZeebeClientConfiguration {

	@Bean
	public ZeebeClient zeebe() {
		final ZeebeClient client = ZeebeClient.newClientBuilder()
				.brokerContactPoint("127.0.0.1:26500")
				.usePlaintext()
				.build();
		return client;
	}

}
