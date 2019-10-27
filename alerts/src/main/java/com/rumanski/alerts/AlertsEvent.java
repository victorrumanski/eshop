package com.rumanski.alerts;

public class AlertsEvent {

	EventType eventType;

	String payload;

}

enum EventType {

	EMAIL_SENT,

	SMS_SENT,

	NOTIFICATION_SENT,

	;
}
