package com.rumanski.basket.es.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.rumanski.basket.model.BasketEvent;
import com.rumanski.basket.repository.BasketEventRepository;

@Service
public class EventStore {

	@Autowired
	BasketEventRepository repo;

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
	public void saveAndPublish(BasketEvent event) {
		repo.save(event);
		applicationEventPublisher.publishEvent(event);
	}

	public void saveOnly(BasketEvent event) {
		repo.save(event);
	}
}
