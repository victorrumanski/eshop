package com.rumanski.warehouse.es.events;

import java.math.BigDecimal;

public class ReservationCompletedEvent extends WarehouseDomainEvent {

	public final Long orderitemid;
	public final Long stockid;
	public final BigDecimal quantity;

	public ReservationCompletedEvent(Long orderitemid, Long stockid, BigDecimal quantity) {
		super(EventType.RESERVATION_COMPLETED);
		this.orderitemid = orderitemid;
		this.stockid = stockid;
		this.quantity = quantity;
	}

}
