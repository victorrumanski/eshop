package com.rumanski.basket.es.events;

import java.util.Date;

public class BasketAbstractEvent {

	public final EventType type;

	public final Date timestamp = new Date();

	public BasketAbstractEvent(EventType type) {
		super();
		this.type = type;
	}

	public static enum EventType {

		ITEM_ADDED,

		ITEM_REMOVED,

	}
}
