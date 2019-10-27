package com.rumanski.catalog.es.events;

import java.util.Date;

public class CatalogDomainEvent {

	public final EventType type;

	public final Date timestamp = new Date();

	public CatalogDomainEvent(EventType type) {
		super();
		this.type = type;
	}

	public static enum EventType {

		PRICE_CHANGED,

		AVAILABILITY_CHANGED,

	}
}
