package com.rumanski.catalog.es.events;

import java.math.BigDecimal;

public class PriceChangedEvent extends CatalogDomainEvent {

	public final Long productid;
	public final BigDecimal newprice;

	public PriceChangedEvent(Long productid, BigDecimal newprice) {
		super(EventType.PRICE_CHANGED);
		this.productid = productid;
		this.newprice = newprice;
	}

}
