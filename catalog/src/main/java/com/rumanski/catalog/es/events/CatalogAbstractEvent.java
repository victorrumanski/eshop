package com.rumanski.catalog.es.events;

import java.util.Date;

public class CatalogAbstractEvent {

	public final EventType type;

	public final Date timestamp = new Date();

	public CatalogAbstractEvent(EventType type) {
		super();
		this.type = type;
	}

	public static enum EventType {

		PRICE_CHANGED,

		AVAILABILITY_CHANGED,

	}
}
