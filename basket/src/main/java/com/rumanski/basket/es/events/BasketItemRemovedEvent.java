package com.rumanski.basket.es.events;

public class BasketItemRemovedEvent extends BasketDomainEvent {

	public final Long basketitemid;

	public BasketItemRemovedEvent(Long basketitemid) {
		super(EventType.ITEM_REMOVED);
		this.basketitemid = basketitemid;
	}

}
