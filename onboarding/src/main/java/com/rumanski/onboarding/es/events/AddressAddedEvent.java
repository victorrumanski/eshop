package com.rumanski.onboarding.es.events;

public class AddressAddedEvent extends OnBoardingAbstractEvent {

	public final Long userid;

	public final String street, city, state, postalCode;

	public AddressAddedEvent(Long userid, String street, String city, String state,
			String postalCode) {
		super(EventType.ADDRESS_ADDED);
		this.userid = userid;
		this.street = street;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
	}

}
