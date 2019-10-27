package com.rumanski.basket.api;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rumanski.basket.model.BasketItem;
import com.rumanski.basket.repository.BasketItemRepository;

@RestController
@RequestMapping("/api/basket")
public class BasketCrudApi {

	/**
	 * !!! THE BIG THING HERE IS THE ABSCENSE OF EVENTS !!!!
	 */

	@Autowired
	BasketItemRepository basketRepo;

	@PutMapping("/")
	public ResponseEntity<BasketItem> updateQuantity(Long basketitemid, BigDecimal quantity) {
		BasketItem x = basketRepo.findById(basketitemid).get();
		x.setQuantity(quantity);
		x = basketRepo.save(x);
		return ResponseEntity.ok().body(x);
	}

}
