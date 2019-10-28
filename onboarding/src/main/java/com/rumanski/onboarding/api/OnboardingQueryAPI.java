package com.rumanski.onboarding.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rumanski.onboarding.ResourceNotFoundException;
import com.rumanski.onboarding.model.Address;
import com.rumanski.onboarding.model.Card;
import com.rumanski.onboarding.model.User;
import com.rumanski.onboarding.repository.AddressRepository;
import com.rumanski.onboarding.repository.CardRepository;
import com.rumanski.onboarding.repository.UserRepository;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingQueryAPI {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AddressRepository addressRepo;

	@Autowired
	private CardRepository cardRepo;

	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUser(@PathVariable(value = "id") Long id) {
		return ResponseEntity.ok().body(userRepo.findById(id).map(user -> {
			user.addresses = addressRepo.findByUseridAndRemoved(user.getId(), false);
			user.cards = cardRepo.findByUserid(user.getId());
			return user;
		}).orElseThrow(() -> new ResourceNotFoundException("USER", "ID", id)));
	}

	@GetMapping("/users/{userid}/addresses")
	public ResponseEntity<List<Address>> listAddress(@PathVariable(value = "userid") Long userid) {
		return ResponseEntity.ok().body(addressRepo.findByUseridAndRemoved(userid, false));
	}

	@GetMapping("/users/{userid}/addresses/{id}")
	public ResponseEntity<Address> getAddress(@PathVariable(value = "id") Long id) {
		return ResponseEntity.ok()
				.body(addressRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("ADDRESS", "ID", id)));
	}

	@GetMapping("/users/{userid}/cards")
	public ResponseEntity<List<Card>> listCards(@PathVariable(value = "userid") Long userid) {
		return ResponseEntity.ok().body(cardRepo.findByUserid(userid));
	}

	@GetMapping("/users/{userid}/cards/{id}")
	public ResponseEntity<Card> getCard(@PathVariable(value = "id") Long id) {
		return ResponseEntity.ok()
				.body(cardRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("CREDITCARD", "ID", id)));
	}

	@GetMapping("/cards/{id}")
	public ResponseEntity<Card> card(@PathVariable(value = "id") Long id) {
		return ResponseEntity.ok()
				.body(cardRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("CREDITCARD", "ID", id)));
	}

	@GetMapping
	public ResponseEntity<List<User>> all() {
		return ResponseEntity.ok().body(userRepo.findAll());
	}

}