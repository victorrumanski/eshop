package com.rumanski.basket.es.events;

import java.util.List;

import com.rumanski.basket.model.BasketItem;

public class OrderPlacedEvent extends BasketDomainEvent {

	public final Long userid, addressid, cardid;

	public final List<BasketItem> items;

	public OrderPlacedEvent(Long userid, Long addressid, Long cardid, List<BasketItem> items) {
		super(EventType.ORDER_PLACED);
		this.userid = userid;
		this.addressid = addressid;
		this.cardid = cardid;
		this.items = items;
	}

}
