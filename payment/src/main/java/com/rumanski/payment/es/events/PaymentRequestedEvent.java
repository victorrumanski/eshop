package com.rumanski.payment.es.events;

import java.math.BigDecimal;

public class PaymentRequestedEvent extends PaymentDomainEvent {

	public final Long orderid;
	public final Long cardid;
	public final BigDecimal amount;

	public PaymentRequestedEvent(Long orderid, Long cardid, BigDecimal amount) {
		super(EventType.PAYMENT_REQUESTED);
		this.orderid = orderid;
		this.cardid = cardid;
		this.amount = amount;
	}

}
