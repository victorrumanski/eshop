package com.rumanski.ordering;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rumanski.ordering.model.Order;
import com.rumanski.ordering.repository.OrderItemRepository;
import com.rumanski.ordering.repository.OrderRepository;

@RestController
@RequestMapping("/api/ordering")
public class OrderingQueryAPI {

	@Autowired
	private OrderRepository orderRepo;

	@Autowired
	private OrderItemRepository orderItemRepo;

	@GetMapping
	public ResponseEntity<List<Order>> all() {
		List<Order> list = orderRepo.findAll();
		list.forEach(o -> {
			o.items = orderItemRepo.findByOrderid(o.getId());
		});
		return ResponseEntity.ok().body(list);
	}

}