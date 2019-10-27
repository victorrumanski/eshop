package com.rumanski.basket.es;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rumanski.basket.es.events.BasketAbstractEvent;
import com.rumanski.basket.es.events.BasketItemAddedEvent;
import com.rumanski.basket.es.events.BasketItemRemovedEvent;
import com.rumanski.basket.es.store.EventStore;
import com.rumanski.basket.model.BasketEvent;
import com.rumanski.basket.model.BasketItem;

@Service
@Transactional
public class BasketCommandHandler {

	@Autowired
	BasketEventHandler eventHandler;

	@Autowired
	EventStore store;

	public BasketItem addItem(Long userid, Long productid, BigDecimal price) {
		BasketItemAddedEvent event = new BasketItemAddedEvent(userid, productid, price);
		BasketItem handle = eventHandler.handle(event);
		store.saveAndPublish(convert(event, handle.getId()));
		return handle;
	}

	public void removeItem(Long basketitemid) {
		BasketItemRemovedEvent event = new BasketItemRemovedEvent(basketitemid);
		eventHandler.handle(event);
		store.saveAndPublish(convert(event, event.basketitemid));
	}

	private BasketEvent convert(BasketAbstractEvent src, Long correlationID) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		String payload;
		try {
			payload = objectMapper.writeValueAsString(src);
		} catch (JsonProcessingException e1) {
			throw new RuntimeException(e1);
		}
		BasketEvent e = new BasketEvent(src.type.name(), src.timestamp, payload);
		e.setCorrelationid(correlationID);
		return e;
	}

}