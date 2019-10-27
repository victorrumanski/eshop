package com.rumanski.onboarding.es.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.rumanski.onboarding.model.OnboardingEvent;
import com.rumanski.onboarding.repository.OnboardingEventRepository;

@Service
public class EventStore {

	@Autowired
	OnboardingEventRepository repo;

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
	public void saveAndPublish(OnboardingEvent event) {
		repo.save(event);
		applicationEventPublisher.publishEvent(event);
	}

	public void saveOnly(OnboardingEvent event) {
		repo.save(event);
	}
}
