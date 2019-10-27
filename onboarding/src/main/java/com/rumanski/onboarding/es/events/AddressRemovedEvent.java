package com.rumanski.onboarding.es.events;

public class AddressRemovedEvent extends OnBoardingDomainEvent {

	public final Long addressid;

	public AddressRemovedEvent(Long addressid) {
		super(EventType.ADDRESS_REMOVED);
		this.addressid = addressid;
	}

}
