package com.rumanski.basket.es;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rumanski.basket.es.events.BasketItemAddedEvent;
import com.rumanski.basket.es.events.BasketItemRemovedEvent;
import com.rumanski.basket.model.BasketItem;
import com.rumanski.basket.model.PriceChangeAlert;
import com.rumanski.basket.repository.BasketItemRepository;
import com.rumanski.basket.repository.PriceChangeAlertRepository;

@Service
public class BasketEventHandler {

	@Autowired
	BasketItemRepository basketRepo;
	@Autowired
	PriceChangeAlertRepository priceChangeAlertRepo;

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

		basketRepo.delete(x);
	}

	public void handlePriceChanged(String message) throws Exception {
		ObjectMapper reader = new ObjectMapper();
		Map<?, ?> obj = reader.readValue(message, Map.class);
		Object productid = obj.get("productid");
		Object newprice = obj.get("newprice");

		List<BasketItem> items = basketRepo.findByProductid(Long.parseLong(productid.toString()));
		items.forEach(item -> {
			PriceChangeAlert priceChangeAlert = new PriceChangeAlert(item.getId(), item.getPrice(),
					new BigDecimal(newprice.toString()));
			priceChangeAlertRepo.save(priceChangeAlert);
		});
	}

}