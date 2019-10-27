package com.rumanski.payment;

public class PaymentEvent {

	EventType eventType;

	String payload;

}

enum EventType {

	PAYMENT_REQUESTED,

	PAYMENT_COMPLETED,

	PAYMENT_FAILED,

	PAYMENT_CANCELED,
	
	PAYMENT_REFUNDED,

}
