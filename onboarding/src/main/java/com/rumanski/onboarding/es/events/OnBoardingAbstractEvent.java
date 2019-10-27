package com.rumanski.onboarding.es.events;

import java.util.Date;

public abstract class OnBoardingAbstractEvent {

	public final EventType type;

	public final Date timestamp = new Date();

	public OnBoardingAbstractEvent(EventType type) {
		super();
		this.type = type;
	}

	public static enum EventType {

		USER_JOINED,

		ADDRESS_ADDED,

		ADDRESS_CHANGED,

		ADDRESS_REMOVED,

		CARD_ADDED,

		CARD_REMOVED,

	}
}
