package com.rumanski.payment.es.events;

import java.math.BigDecimal;

public class PaymentCompletedEvent extends PaymentDomainEvent {

	public final Long orderid;
	public final Long cardid;
	public final BigDecimal amount;

	public PaymentCompletedEvent(Long orderid, Long cardid, BigDecimal amount) {
		super(EventType.PAYMENT_COMPLETED);
		this.orderid = orderid;
		this.cardid = cardid;
		this.amount = amount;
	}

}
