package com.rumanski.warehouse.api;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rumanski.warehouse.model.Stock;
import com.rumanski.warehouse.repository.StockRepository;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseCrudApi {

	/**
	 * !!! THE BIG THING HERE IS THE ABSCENSE OF EVENTS !!!!
	 */

	@Autowired
	StockRepository stockRepo;

	@PostMapping("/stocks")
	public ResponseEntity<Stock> createStock(Long productid, BigDecimal total) {
		Stock x = new Stock();
		x.setProductid(productid);
		x.setTotal(total);
		x.setReserved(BigDecimal.ZERO);
		x.setAvailable(total);
		x = stockRepo.save(x);
		return ResponseEntity.ok().body(x);
	}

	@PutMapping("/stocks/{id}/set-total")
	public ResponseEntity<Stock> updateTotal(@PathVariable(value = "id") Long id, BigDecimal total) {
		Stock x = stockRepo.findById(id).get();
		x.setTotal(total);
		x.setAvailable(x.getTotal().subtract(x.getReserved()));
		x = stockRepo.save(x);
		return ResponseEntity.ok().body(x);
	}

}
