package com.rumanski.basket.es.events;

import java.math.BigDecimal;
import java.util.List;

public class BasketPriceChangedEvent extends BasketDomainEvent {

	public final Long productid;
	public final BigDecimal newprice;
	public final List<Long> userids;

	public BasketPriceChangedEvent(Long productid, BigDecimal newprice, List<Long> userids) {
		super(EventType.BASKET_PRICE_CHANGED);
		this.productid = productid;
		this.newprice = newprice;
		this.userids = userids;
	}

}
