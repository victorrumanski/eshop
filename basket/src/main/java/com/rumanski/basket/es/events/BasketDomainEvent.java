package com.rumanski.basket.es.events;

import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rumanski.basket.model.BasketEvent;

public class BasketDomainEvent {

	public final EventType type;

	public final Date timestamp = new Date();

	public BasketDomainEvent(EventType type) {
		super();
		this.type = type;
	}

	public BasketEvent toEventTable(Long correlationID) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		String payload;
		try {
			payload = objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e1) {
			throw new RuntimeException(e1);
		}
		BasketEvent e = new BasketEvent(this.type.name(), this.timestamp, payload);
		e.setCorrelationid(correlationID);
		return e;
	}

	public static enum EventType {

		ITEM_ADDED,

		ITEM_REMOVED,

		BASKET_CLEARED,

		ORDER_PLACED,

		BASKET_PRICE_CHANGED,
	}
}
