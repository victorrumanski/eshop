package com.rumanski.catalog.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rumanski.catalog.ResourceNotFoundException;
import com.rumanski.catalog.model.Product;
import com.rumanski.catalog.repository.ProductRepository;

@RestController
@RequestMapping("/api/catalog")
public class CatalogQueryAPI {

	@Autowired
	private ProductRepository productRepo;

	@GetMapping("/products")
	public ResponseEntity<List<Product>> getProducts() {
		return ResponseEntity.ok().body(productRepo.findAll());
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable(value = "id") Long id) {
		return ResponseEntity.ok().body(
				productRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("PRODUCT", "ID", id)));
	}

}