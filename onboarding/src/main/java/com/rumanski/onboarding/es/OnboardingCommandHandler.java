package com.rumanski.onboarding.es;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rumanski.onboarding.es.events.AddressAddedEvent;
import com.rumanski.onboarding.es.events.AddressChangedEvent;
import com.rumanski.onboarding.es.events.AddressRemovedEvent;
import com.rumanski.onboarding.es.events.CardAddedEvent;
import com.rumanski.onboarding.es.events.CardRemovedEvent;
import com.rumanski.onboarding.es.events.OnBoardingAbstractEvent;
import com.rumanski.onboarding.es.events.UserJoinedEvent;
import com.rumanski.onboarding.es.store.EventStore;
import com.rumanski.onboarding.model.Address;
import com.rumanski.onboarding.model.Card;
import com.rumanski.onboarding.model.OnboardingEvent;
import com.rumanski.onboarding.model.User;

@Service
@Transactional
public class OnboardingCommandHandler {

	@Autowired
	OnboardingEventHandler eventHandler;

	@Autowired
	EventStore store;

	public User join(
			@NotBlank(message = "Name is mandatory") String name,
			@NotBlank(message = "Email is mandatory") String email,
			@NotBlank(message = "Pass is mandatory") String password) {
		UserJoinedEvent event = new UserJoinedEvent(name, email, password);
		User handle = eventHandler.handle(event);
		store.saveAndPublish(convert(event, handle.getId()));
		return handle;
	}

	public Address createAddress(Long userid, String street, String city, String state, String postalCode) {
		AddressAddedEvent event = new AddressAddedEvent(userid, street, city, state, postalCode);
		Address handle = eventHandler.handle(event);
		store.saveAndPublish(convert(event, handle.getId()));
		return handle;
	}

	public Address updateAddress(Long addressid, String street, String city, String state, String postalCode) {
		AddressChangedEvent event = new AddressChangedEvent(addressid, street, city, state, postalCode);
		store.saveAndPublish(convert(event, addressid));
		return eventHandler.handle(event);
	}

	public void removeAddress(Long addressid) {
		AddressRemovedEvent event = new AddressRemovedEvent(addressid);
		eventHandler.handle(event);
		store.saveAndPublish(convert(event, addressid));
	}

	public Card createCreditCard(Long userid, String number, Date validUntil, String name,
			String verificationCode) {
		CardAddedEvent event = new CardAddedEvent(userid, validUntil, number, name, verificationCode);
		Card handle = eventHandler.handle(event);
		store.saveAndPublish(convert(event, handle.getId()));
		return handle;
	}

	public void removeCreditCard(Long cardid) {
		CardRemovedEvent event = new CardRemovedEvent(cardid);
		eventHandler.handle(event);
		store.saveAndPublish(convert(event, cardid));
	}

	private OnboardingEvent convert(OnBoardingAbstractEvent src, Long correlationID) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		String payload;
		try {
			payload = objectMapper.writeValueAsString(src);
		} catch (JsonProcessingException e1) {
			throw new RuntimeException(e1);
		}
		OnboardingEvent e = new OnboardingEvent(src.type.name(), src.timestamp, payload);
		e.setCorrelationid(correlationID);
		return e;
	}

}
