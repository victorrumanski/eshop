package com.rumanski.onboarding.es.events;

public class UserJoinedEvent extends OnBoardingDomainEvent {

	public final String name, email, password;

	public UserJoinedEvent(String name, String email, String password) {
		super(EventType.USER_JOINED);
		this.name = name;
		this.email = email;
		this.password = password;
	}

}
