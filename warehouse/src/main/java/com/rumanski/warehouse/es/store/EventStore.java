package com.rumanski.warehouse.es.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.rumanski.warehouse.model.WarehouseEvent;
import com.rumanski.warehouse.repository.WarehouseEventRepository;

@Service
public class EventStore {

	@Autowired
	WarehouseEventRepository repo;

	@Autowired
	ApplicationEventPublisher applicationEventPublisher;

	/**
	 * Saves the event to DB
	 * 
	 * Publishes the event to Spring pub-sub, hoping a transaction will commit and
	 * its listeners will receive the event
	 * 
	 * @param event
	 */
	public void saveAndPublish(WarehouseEvent event) {
		repo.save(event);
		repo.flush();
		applicationEventPublisher.publishEvent(event);
	}

	public void saveOnly(WarehouseEvent event) {
		repo.save(event);
	}
}
