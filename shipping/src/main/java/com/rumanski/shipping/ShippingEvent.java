package com.rumanski.shipping;

public class ShippingEvent {

	EventType eventType;

	String payload;

}

enum EventType {

	SHIPPING_COMPLETED,
	
	SHIPPING_FAILED,

	SHIPPING_CANCELED,

}
