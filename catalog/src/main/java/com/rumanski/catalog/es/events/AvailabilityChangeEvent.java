package com.rumanski.catalog.es.events;

public class AvailabilityChangeEvent extends CatalogAbstractEvent {

	public final Long productid;
	public final boolean available;

	public AvailabilityChangeEvent(Long productid, boolean available) {
		super(EventType.AVAILABILITY_CHANGED);
		this.productid = productid;
		this.available = available;
	}

}
