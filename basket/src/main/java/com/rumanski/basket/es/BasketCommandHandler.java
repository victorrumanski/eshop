package com.rumanski.basket.es;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rumanski.basket.es.events.BasketClearedEvent;
import com.rumanski.basket.es.events.BasketItemAddedEvent;
import com.rumanski.basket.es.events.BasketItemRemovedEvent;
import com.rumanski.basket.es.events.OrderPlacedEvent;
import com.rumanski.basket.es.store.EventStore;
import com.rumanski.basket.model.BasketItem;
import com.rumanski.basket.repository.BasketItemRepository;

@Service
@Transactional
public class BasketCommandHandler {

	@Autowired
	BasketEventHandler eventHandler;

	@Autowired
	EventStore store;

	@Autowired
	BasketItemRepository basketRepo;

	public BasketItem addItem(Long userid, Long productid, BigDecimal price) {
		BasketItemAddedEvent event = new BasketItemAddedEvent(userid, productid, price);
		BasketItem handle = eventHandler.handle(event);
		store.saveAndPublish(event.toEventTable(handle.getId()));
		return handle;
	}

	public void removeItem(Long basketitemid) {
		BasketItemRemovedEvent event = new BasketItemRemovedEvent(basketitemid);
		eventHandler.handle(event);
		store.saveAndPublish(event.toEventTable(event.basketitemid));
	}

	public void clear(Long userid) {
		BasketClearedEvent event = new BasketClearedEvent(userid);
		store.saveAndPublish(event.toEventTable(event.userid));

		List<BasketItem> items = basketRepo.findByUserid(event.userid);
		items.forEach(item -> {
			removeItem(item.getId());
		});
	}

	public void placeOrder(Long userid, Long addressid, Long cardid) {
		List<BasketItem> items = basketRepo.findByUserid(userid);
		OrderPlacedEvent event = new OrderPlacedEvent(userid, addressid, cardid, items);
		eventHandler.handle(event);
		store.saveAndPublish(event.toEventTable(event.userid));
	}

}