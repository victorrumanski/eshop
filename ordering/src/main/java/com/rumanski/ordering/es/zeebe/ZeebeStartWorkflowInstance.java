package com.rumanski.ordering.es.zeebe;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.WorkflowInstanceEvent;

public class ZeebeStartWorkflowInstance {

	public static void main(String[] args) {

		final ZeebeClient client = ZeebeClient.newClientBuilder()
				.brokerContactPoint("127.0.0.1:26500")
				.usePlaintext()
				.build();

		System.out.println("Connected.");
		
		final Map<String, Object> data = new HashMap<>();
        data.put("orderId", 31243);
        data.put("orderItems", Arrays.asList(435, 182, 376));

		final WorkflowInstanceEvent wfInstance = client.newCreateInstanceCommand()
				.bpmnProcessId("place-order")
				.latestVersion()
				.variables(data)
				.send()
				.join();

		final long workflowInstanceKey = wfInstance.getWorkflowInstanceKey();

		System.out.println("Workflow instance created. Key: " + workflowInstanceKey);

		client.close();
		System.out.println("Closed.");
	}
}
