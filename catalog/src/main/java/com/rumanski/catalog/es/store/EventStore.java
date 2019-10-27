package com.rumanski.catalog.es.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.rumanski.catalog.model.CatalogEvent;
import com.rumanski.catalog.repository.CatalogEventRepository;

@Service
public class EventStore {

	@Autowired
	CatalogEventRepository repo;

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
	public void saveAndPublish(CatalogEvent event) {
		repo.save(event);
		applicationEventPublisher.publishEvent(event);
	}

	public void saveOnly(CatalogEvent event) {
		repo.save(event);
	}
}
