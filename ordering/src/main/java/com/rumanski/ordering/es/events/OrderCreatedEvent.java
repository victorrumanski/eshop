package com.rumanski.ordering.es.events;

import com.rumanski.ordering.model.Order;

public class OrderCreatedEvent extends OrderingDomainEvent {

	public final Order order;

	public OrderCreatedEvent(Order order) {
		super(EventType.ORDER_CREATED);
		this.order = order;
	}

}
