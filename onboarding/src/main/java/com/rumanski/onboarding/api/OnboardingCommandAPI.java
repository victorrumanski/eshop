package com.rumanski.onboarding.api;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rumanski.onboarding.es.OnboardingCommandHandler;
import com.rumanski.onboarding.model.Address;
import com.rumanski.onboarding.model.Card;
import com.rumanski.onboarding.model.User;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingCommandAPI {

	@Autowired
	private OnboardingCommandHandler commands;

	@PostMapping("/users")
	public ResponseEntity<User> join(String name, String email, String password) {
		User newUser = commands.join(name, email, password);
		return ResponseEntity.ok().body(newUser);
	}

	@PostMapping("/users/{userid}/addresses")
	public ResponseEntity<Address> createAddress(@PathVariable(value = "userid") Long userid, String street,
			String city, String state, String postalCode) {
		Address x = commands.createAddress(userid, street, city, state, postalCode);
		return ResponseEntity.ok().body(x);
	}

	@PutMapping("/users/{userid}/addresses/{id}")
	public ResponseEntity<Address> updateAddress(@PathVariable(value = "id") Long id, String street, String city,
			String state, String postalCode) {
		Address x = commands.updateAddress(id, street, city, state, postalCode);
		return ResponseEntity.ok().body(x);
	}

	@DeleteMapping("/users/{userid}/addresses/{id}")
	public ResponseEntity<Void> removeAddress(@PathVariable(value = "id") Long id) {
		commands.removeAddress(id);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/users/{userid}/cards")
	public ResponseEntity<Card> createCreditCard(@PathVariable(value = "userid") Long userid, String number,
			@DateTimeFormat(pattern = "MM/yyyy") Date validUntil, String name, String verificationCode) {
		Card x = commands.createCreditCard(userid, number, validUntil, name, verificationCode);
		return ResponseEntity.ok().body(x);
	}

	@PutMapping("/users/{userid}/cards/{id}")
	public ResponseEntity<Void> removeCreditCard(@PathVariable(value = "id") Long id) {
		commands.removeCreditCard(id);
		return ResponseEntity.ok().build();
	}

}