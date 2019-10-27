package com.rumanski.ordering.es.zeebe;

import java.util.HashMap;
import java.util.Map;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.worker.JobWorker;

public class ZeebeReserveStocksCancellationJobWorker {

	public static void main(String[] args) {

		final ZeebeClient client = ZeebeClient.newClientBuilder()
				.brokerContactPoint("127.0.0.1:26500")
				.usePlaintext()
				.build();
		System.out.println("Connected.");

		final JobWorker jobWorker = client.newWorker()
				.jobType("reserve-stocks-cancelation")
				.handler((jobClient, job) -> {
					final Map<String, Object> variables = job.getVariablesAsMap();

					System.out.println("Reserve stocks: " + variables.get("orderId"));
					final Map<String, Object> result = new HashMap<>();
					result.put("reservation-canceled", true);

					jobClient.newCompleteCommand(job.getKey())
							.variables(result)
							.send()
							.join();
				})
				// .fetchVariables("orderId")
				.open();
		// Don't close, we need to keep polling to get work
		// jobWorker.close();

		// client.close();
		// System.out.println("Closed.");
	}
}
