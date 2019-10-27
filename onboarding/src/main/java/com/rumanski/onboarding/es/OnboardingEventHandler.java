package com.rumanski.onboarding.es;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rumanski.onboarding.es.events.AddressAddedEvent;
import com.rumanski.onboarding.es.events.AddressChangedEvent;
import com.rumanski.onboarding.es.events.AddressRemovedEvent;
import com.rumanski.onboarding.es.events.CardAddedEvent;
import com.rumanski.onboarding.es.events.CardRemovedEvent;
import com.rumanski.onboarding.es.events.UserJoinedEvent;
import com.rumanski.onboarding.model.Address;
import com.rumanski.onboarding.model.Card;
import com.rumanski.onboarding.model.User;
import com.rumanski.onboarding.repository.AddressRepository;
import com.rumanski.onboarding.repository.CardRepository;
import com.rumanski.onboarding.repository.UserRepository;

@Service
public class OnboardingEventHandler {

	@Autowired
	UserRepository userRepo;

	@Autowired
	CardRepository cardRepo;

	@Autowired
	AddressRepository addressRepo;

	public User handle(UserJoinedEvent event) {
		User u = new User();
		u.setJoined(new Date());
		u.setName(event.name);
		u.setEmail(event.email);
		u.setPassword(event.password);
		u = userRepo.save(u);
		return u;
	}

	public Address handle(AddressAddedEvent event) {
		User user = userRepo.findById(event.userid)
				.orElseThrow(() -> new IllegalArgumentException("USER ID " + event.userid + " not found"));

		Address x = new Address();
		x.setUserid(user.getId());
		x.setStreet(event.street);
		x.setCity(event.city);
		x.setPostalcode(event.postalCode);
		x.setState(event.state);
		x = addressRepo.save(x);
		return x;
	}

	public Address handle(AddressChangedEvent event) {
		Address x = addressRepo.findById(event.addressid)
				.orElseThrow(() -> new IllegalArgumentException("ADDRESS ID " + event.addressid + " not found"));

		x.setStreet(event.street);
		x.setCity(event.city);
		x.setPostalcode(event.postalCode);
		x.setState(event.state);
		x = addressRepo.save(x);
		return x;
	}

	public void handle(AddressRemovedEvent event) {
		Address x = addressRepo.findById(event.addressid)
				.orElseThrow(() -> new IllegalArgumentException("ADDRESS ID " + event.addressid + " not found"));
		x.setRemoved(true);
		x = addressRepo.save(x);
	}

	public Card handle(CardAddedEvent event) {
		User user = userRepo.findById(event.userid)
				.orElseThrow(() -> new IllegalArgumentException("USER ID " + event.userid + " not found"));
		Card x = new Card();
		x.setUserid(user.getId());
		x.setNumber(event.number);
		x.setName(event.name);
		x.setValiduntil(event.validUntil);
		x.setVerificationcode(event.verificationCode);
		x = cardRepo.save(x);
		return x;
	}

	public Card handle(CardRemovedEvent event) {
		Card x = cardRepo.findById(event.cardid)
				.orElseThrow(() -> new IllegalArgumentException("CARD ID " + event.cardid + " not found"));
		x.setRemoved(true);
		x = cardRepo.save(x);
		return x;
	}

}
