package com.rumanski.onboarding.es.events;

import java.util.Date;

public class CardAddedEvent extends OnBoardingDomainEvent {

	public final Long userid;

	public final Date validUntil;
	public final String number, name, verificationCode;

	public CardAddedEvent(Long userid, Date validUntil, String number, String name,
			String verificationCode) {
		super(EventType.CARD_ADDED);
		this.userid = userid;
		this.validUntil = validUntil;
		this.number = number;
		this.name = name;
		this.verificationCode = verificationCode;
	}

}
