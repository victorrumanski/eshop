package com.rumanski.basket.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rumanski.basket.model.BasketItem;
import com.rumanski.basket.repository.BasketItemRepository;
import com.rumanski.basket.repository.PriceChangeAlertRepository;

@RestController
@RequestMapping("/api/basket")
public class BasketQueryAPI {

	@Autowired
	private BasketItemRepository basketRepo;

	@Autowired
	private PriceChangeAlertRepository priceChangeAlertRepository;

	@GetMapping("/{userid}")
	public ResponseEntity<List<BasketItem>> getBasket(@PathVariable(value = "userid") Long userid) {
		return ResponseEntity.ok().body(basketRepo.findByUserid(userid).stream().map(i -> {
			i.setPriceChanges(priceChangeAlertRepository.findByBasketitemid(i.getId()));
			return i;
		}).collect(Collectors.toList()));
	}

}