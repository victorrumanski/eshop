package com.rumanski.catalog.api;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rumanski.catalog.es.CatalogCommandHandler;

@RestController
@RequestMapping("/api/catalog")
public class CatalogCommandAPI {

	@Autowired
	private CatalogCommandHandler commands;

	@PostMapping("/products/{productid}/price")
	public ResponseEntity<Void> changePrice(@PathVariable(value = "productid") Long productid, BigDecimal price) {
		commands.changePrice(productid, price);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/products/{productid}/availability")
	public ResponseEntity<Void> changeAvailability(@PathVariable(value = "productid") Long productid,
			boolean availability) {
		commands.changeAvailability(productid, availability);
		return ResponseEntity.ok().build();
	}
}