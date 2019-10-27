package com.rumanski.basket.api;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rumanski.basket.es.BasketCommandHandler;
import com.rumanski.basket.model.BasketItem;

@RestController
@RequestMapping("/api/basket")
public class BasketCommandAPI {

	@Autowired
	private BasketCommandHandler commands;

	@PostMapping("/{userid}")
	public ResponseEntity<BasketItem> addItem(@PathVariable(value = "userid") Long userid, Long productid,
			BigDecimal price) {
		BasketItem item = commands.addItem(userid, productid, price);
		return ResponseEntity.ok().body(item);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> removeItem(@PathVariable(value = "id") Long id) {
		commands.removeItem(id);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{userid}/clear")
	public ResponseEntity<Void> clear(@PathVariable(value = "userid") Long userid) {
		commands.clear(userid);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{userid}/placeorder")
	public ResponseEntity<Void> placeOrder(@PathVariable(value = "userid") Long userid, Long addressid, Long cardid) {
		commands.placeOrder(userid, addressid, cardid);
		return ResponseEntity.ok().build();
	}

}