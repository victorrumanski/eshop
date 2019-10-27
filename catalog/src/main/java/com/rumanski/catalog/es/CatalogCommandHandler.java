package com.rumanski.catalog.es;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rumanski.catalog.es.events.AvailabilityChangeEvent;
import com.rumanski.catalog.es.events.CatalogAbstractEvent;
import com.rumanski.catalog.es.events.PriceChangeEvent;
import com.rumanski.catalog.es.store.EventStore;
import com.rumanski.catalog.model.CatalogEvent;

@Service
@Transactional
public class CatalogCommandHandler {

	@Autowired
	CatalogEventHandler eventHandler;

	@Autowired
	EventStore store;

	public void changePrice(Long productid, BigDecimal price) {
		PriceChangeEvent event = new PriceChangeEvent(productid, price);
		eventHandler.handle(event);
		store.saveAndPublish(convert(event, event.productid));
	}

	public void changeAvailability(Long productid, boolean available) {
		AvailabilityChangeEvent event = new AvailabilityChangeEvent(productid, available);
		eventHandler.handle(event);
		store.saveAndPublish(convert(event, event.productid));
	}

	private CatalogEvent convert(CatalogAbstractEvent src, Long correlationID) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		String payload;
		try {
			payload = objectMapper.writeValueAsString(src);
		} catch (JsonProcessingException e1) {
			throw new RuntimeException(e1);
		}
		CatalogEvent e = new CatalogEvent(src.type.name(), src.timestamp, payload);
		e.setCorrelationid(correlationID);
		return e;
	}

}