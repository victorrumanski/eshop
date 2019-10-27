package com.rumanski.basket.es.events;

import java.math.BigDecimal;

public class BasketItemAddedEvent extends BasketDomainEvent {

	public final Long userid;
	public final Long productid;
	public final BigDecimal price;

	public BasketItemAddedEvent(Long userid, Long productid, BigDecimal price) {
		super(EventType.ITEM_ADDED);
		this.userid = userid;
		this.productid = productid;
		this.price = price;
	}

}
