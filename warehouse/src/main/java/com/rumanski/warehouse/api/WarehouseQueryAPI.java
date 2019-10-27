package com.rumanski.warehouse.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rumanski.warehouse.model.Reservation;
import com.rumanski.warehouse.model.Stock;
import com.rumanski.warehouse.repository.ReservationRepository;
import com.rumanski.warehouse.repository.StockRepository;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseQueryAPI {

	@Autowired
	StockRepository stockRepo;
	@Autowired
	ReservationRepository reservationRepo;

	@GetMapping("/stocks")
	public ResponseEntity<List<Stock>> stocks() {
		return ResponseEntity.ok().body(stockRepo.findAll());
	}

	@GetMapping("/reservations")
	public ResponseEntity<List<Reservation>> res() {
		return ResponseEntity.ok().body(reservationRepo.findAll());
	}

}