package com.rumanski.basket.es;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rumanski.basket.es.events.BasketItemAddedEvent;
import com.rumanski.basket.es.events.BasketItemRemovedEvent;
import com.rumanski.basket.es.events.BasketPriceChangedEvent;
import com.rumanski.basket.es.events.OrderPlacedEvent;
import com.rumanski.basket.es.store.EventStore;
import com.rumanski.basket.model.BasketItem;
import com.rumanski.basket.model.PriceChangeAlert;
import com.rumanski.basket.repository.BasketItemRepository;
import com.rumanski.basket.repository.PriceChangeAlertRepository;

@Service
@Transactional
public class BasketEventHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(BasketEventHandler.class);

	@Autowired
	BasketItemRepository basketRepo;

	@Autowired
	PriceChangeAlertRepository priceChangeAlertRepo;

	@Autowired
	EventStore store;

	public BasketItem handle(BasketItemAddedEvent event) {
		BasketItem x = new BasketItem();
		x.setUserid(event.userid);
		x.setProductid(event.productid);
		x.setPrice(event.price);
		x.setQuantity(BigDecimal.ONE);
		x = basketRepo.save(x);
		return x;
	}

	public void handle(BasketItemRemovedEvent event) {
		BasketItem x = basketRepo.findById(event.basketitemid)
				.orElseThrow(() -> new IllegalArgumentException("BASKETITEMID " + event.basketitemid + " not found"));

		List<PriceChangeAlert> alerts = priceChangeAlertRepo.findByBasketitemid(x.getId());
		alerts.forEach(a -> {
			priceChangeAlertRepo.delete(a);
		});

		basketRepo.delete(x);
	}

	public void handle(OrderPlacedEvent event) {
		LOGGER.info("OrderPlacedEvent do nothing handler ");
	}

	public void handlePriceChanged(String message) throws Exception {
		LOGGER.info("handlePriceChanged: " + message);
		ObjectMapper reader = new ObjectMapper();
		Map<?, ?> obj = reader.readValue(message, Map.class);
		Long productid = Long.parseLong(obj.get("productid").toString());
		BigDecimal newprice = new BigDecimal(obj.get("newprice").toString());

		List<BasketItem> items = basketRepo.findByProductid(productid);
		items.forEach(item -> priceChangeAlertRepo.save(new PriceChangeAlert(item.getId(), item.getPrice(), newprice)));
		List<Long> userids = items.stream().map(item -> item.getUserid()).distinct().collect(Collectors.toList());
		BasketPriceChangedEvent internalEvent = new BasketPriceChangedEvent(productid, newprice, userids);
		store.saveAndPublish(internalEvent.toEventTable(productid));
	}

}