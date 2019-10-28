package com.rumanski.payment.es;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.rumanski.payment.model.PaymentEvent;
import com.rumanski.payment.repository.PaymentEventRepository;

@Service
public class EventStore {

	@Autowired
	PaymentEventRepository repo;

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
	public void saveAndPublish(PaymentEvent event) {
		repo.save(event);
		repo.flush();
		applicationEventPublisher.publishEvent(event);
	}

	public void saveOnly(PaymentEvent event) {
		repo.save(event);
	}
}
