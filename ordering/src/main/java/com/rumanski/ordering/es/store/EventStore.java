package com.rumanski.ordering.es.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.rumanski.ordering.model.OrderingEvent;
import com.rumanski.ordering.repository.OrderingEventRepository;

@Service
public class EventStore {

	@Autowired
	OrderingEventRepository repo;

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
	public void saveAndPublish(OrderingEvent event) {
		repo.save(event);
		repo.flush();
		applicationEventPublisher.publishEvent(event);
	}

	public void saveOnly(OrderingEvent event) {
		repo.save(event);
	}
}
