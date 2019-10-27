package com.rumanski.warehouse.es.events;

import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rumanski.warehouse.model.WarehouseEvent;

public class WarehouseDomainEvent {

	public final EventType type;

	public final Date timestamp = new Date();

	public WarehouseDomainEvent(EventType type) {
		super();
		this.type = type;
	}

	public WarehouseEvent toEventTable(Long correlationID) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		String payload;
		try {
			payload = objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e1) {
			throw new RuntimeException(e1);
		}
		WarehouseEvent e = new WarehouseEvent(this.type.name(), this.timestamp, payload);
		e.setCorrelationid(correlationID);
		return e;
	}

	public static enum EventType {

		RESERVATION_COMPLETED,

		RESERVATION_CANCELED,

		PICKING_COMPLETED,

		PICKING_CANCELED,

	}
}