package com.rumanski.payment.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rumanski.payment.model.Payment;
import com.rumanski.payment.repository.PaymentRepository;

@RestController
@RequestMapping("/api/payment")
public class PaymentQueryAPI {

	@Autowired
	PaymentRepository paymentRepo;

	@GetMapping("/all")
	public ResponseEntity<List<Payment>> all() {
		return ResponseEntity.ok().body(paymentRepo.findAll());
	}

}