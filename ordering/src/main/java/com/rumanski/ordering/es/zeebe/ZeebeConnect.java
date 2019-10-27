package com.rumanski.ordering.es.zeebe;

import io.zeebe.client.ZeebeClient;

public class ZeebeConnect {

	public static void main(String[] args) {
		// this class will start a new instance of the order bpm workflow

		final ZeebeClient client = ZeebeClient.newClientBuilder()
				// change the contact point if needed
				.brokerContactPoint("127.0.0.1:26500")
				.usePlaintext()
				.build();

		System.out.println("Connected.");

		// ...

		client.close();
		System.out.println("Closed.");
	}

}
