package com.rumanski.catalog.es;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rumanski.catalog.es.events.AvailabilityChangeEvent;
import com.rumanski.catalog.es.events.PriceChangedEvent;
import com.rumanski.catalog.model.Product;
import com.rumanski.catalog.repository.ProductRepository;

@Service
public class CatalogEventHandler {

	@Autowired
	ProductRepository productRepo;

	public void handle(PriceChangedEvent event) {
		Product x = productRepo.findById(event.productid)
				.orElseThrow(() -> new IllegalArgumentException("PRODUCT ID " + event.productid + " not found"));

		x.setPrice(event.newprice);
		x = productRepo.save(x);
	}

	public void handle(AvailabilityChangeEvent event) {
		Product x = productRepo.findById(event.productid)
				.orElseThrow(() -> new IllegalArgumentException("PRODUCT ID " + event.productid + " not found"));

		x.setAvailable(event.available);
		x = productRepo.save(x);
	}

}