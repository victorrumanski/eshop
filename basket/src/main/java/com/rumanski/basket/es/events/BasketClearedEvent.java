package com.rumanski.basket.es.events;

public class BasketClearedEvent extends BasketDomainEvent {

	public final Long userid;

	public BasketClearedEvent(Long userid) {
		super(EventType.BASKET_CLEARED);
		this.userid = userid;
	}

}
