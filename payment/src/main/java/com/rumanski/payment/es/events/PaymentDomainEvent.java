package com.rumanski.payment.es.events;

import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rumanski.payment.model.PaymentEvent;

public class PaymentDomainEvent {

	public final EventType type;

	public final Date timestamp = new Date();

	public PaymentDomainEvent(EventType type) {
		super();
		this.type = type;
	}

	public PaymentEvent toEventTable(Long correlationID) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		String payload;
		try {
			payload = objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e1) {
			throw new RuntimeException(e1);
		}
		PaymentEvent e = new PaymentEvent(this.type.name(), this.timestamp, payload);
		e.setCorrelationid(correlationID);
		return e;
	}

	public static enum EventType {

		PAYMENT_REQUESTED,

		PAYMENT_COMPLETED,

		PAYMENT_FAILED,

		PAYMENT_REFUNDED,

	}
}
