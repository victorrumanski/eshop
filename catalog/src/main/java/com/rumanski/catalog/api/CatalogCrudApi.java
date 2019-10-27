package com.rumanski.catalog.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rumanski.catalog.model.Product;
import com.rumanski.catalog.repository.ProductRepository;

@RestController
@RequestMapping("/api/catalog")
public class CatalogCrudApi {

	/**
	 * !!! THE BIG THING HERE IS THE ABSCENSE OF EVENTS !!!!
	 */

	@Autowired
	ProductRepository productRepo;

	@PostMapping("/products")
	public ResponseEntity<Product> createProduct(String name, String description) {
		Product x = new Product();
		x.setName(name);
		x.setDescription(description);
		x = productRepo.save(x);
		return ResponseEntity.ok().body(x);
	}

	@PutMapping("/products/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable(value = "id") Long id, String name, String description) {
		Product x = productRepo.findById(id).get();
		x.setName(name);
		x.setDescription(description);
		x = productRepo.save(x);
		return ResponseEntity.ok().body(x);
	}
}
