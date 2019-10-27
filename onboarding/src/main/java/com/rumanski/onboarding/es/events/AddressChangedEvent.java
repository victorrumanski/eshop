package com.rumanski.onboarding.es.events;

public class AddressChangedEvent extends OnBoardingAbstractEvent {

	public final Long addressid;

	public final String street, city, state, postalCode;

	public AddressChangedEvent(Long addressid, String street, String city, String state,
			String postalCode) {
		super(EventType.ADDRESS_CHANGED);
		this.addressid = addressid;
		this.street = street;
		this.city = city;
		this.state = state;
		this.postalCode = postalCode;
	}
	
	

}
