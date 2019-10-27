package com.rumanski.ordering.es;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rumanski.ordering.es.store.EventStore;
import com.rumanski.ordering.repository.OrderItemRepository;
import com.rumanski.ordering.repository.OrderRepository;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.WorkflowInstanceEvent;

@Service
@Transactional
public class OrderingEventHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderingEventHandler.class);

	@Autowired
	OrderItemRepository orderItemRepo;

	@Autowired
	OrderRepository orderRepo;

	@Autowired
	EventStore store;

//	@Autowired
//	private ZeebeClientLifecycle zeebeClient;

	@Autowired
	private ZeebeClient zeebeClient;

	public void handleOrderPlaced(String message) throws Exception {
		LOGGER.info("handleOrderPlaced: " + message);

		// start the workflow instance

		final WorkflowInstanceEvent event = zeebeClient
				.newCreateInstanceCommand()
				.bpmnProcessId("place-order")
				.latestVersion()
				.variables(message)
				.send()
				.join();

		LOGGER.info(
				"started instance for workflowKey='{}', bpmnProcessId='{}', version='{}' with workflowInstanceKey='{}'",
				event.getWorkflowKey(), event.getBpmnProcessId(), event.getVersion(), event.getWorkflowInstanceKey());

	}

}