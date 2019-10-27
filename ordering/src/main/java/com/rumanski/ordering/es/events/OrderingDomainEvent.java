package com.rumanski.ordering.es.events;

import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rumanski.ordering.model.OrderingEvent;

public class OrderingDomainEvent {

	public final EventType type;

	public final Date timestamp = new Date();

	public OrderingDomainEvent(EventType type) {
		super();
		this.type = type;
	}

	public OrderingEvent toEventTable(Long correlationID) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		String payload;
		try {
			payload = objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e1) {
			throw new RuntimeException(e1);
		}
		OrderingEvent e = new OrderingEvent(this.type.name(), this.timestamp, payload);
		e.setCorrelationid(correlationID);
		return e;
	}

	public static enum EventType {

		ORDER_CREATED, ORDER_RESERVED, ORDER_PAID, ORDER_PICKED, ORDER_SHIPPED, ORDER_COMPLETED,

		SHIPPING_CANCELED, PICKING_CANCELED, PAYMENT_CANCELED, RESERVATION_CANCELED, ORDER_CANCELED

	}
}
