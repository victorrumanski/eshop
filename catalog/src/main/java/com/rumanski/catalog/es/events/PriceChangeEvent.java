package com.rumanski.catalog.es.events;

import java.math.BigDecimal;

public class PriceChangeEvent extends CatalogAbstractEvent {

	public final Long productid;
	public final BigDecimal newprice;

	public PriceChangeEvent(Long productid, BigDecimal newprice) {
		super(EventType.PRICE_CHANGED);
		this.productid = productid;
		this.newprice = newprice;
	}

}
