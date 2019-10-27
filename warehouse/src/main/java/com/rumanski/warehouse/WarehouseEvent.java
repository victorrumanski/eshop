package com.rumanski.warehouse;

public class WarehouseEvent {

	EventType eventType;

	String payload;

}

enum EventType {

	RESERVATION_COMPLETED,

	RESERVATION_FAILED,

	RESERVATION_CANCELED,

	PICKING_COMPLETED,

	PICKING_FAILED,

	PICKING_CANCELED,

}
