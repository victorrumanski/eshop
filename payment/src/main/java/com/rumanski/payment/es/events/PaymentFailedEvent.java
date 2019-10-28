package com.rumanski.payment.es.events;

import java.math.BigDecimal;

public class PaymentFailedEvent extends PaymentDomainEvent {

	public final Long orderid;
	public final Long cardid;
	public final BigDecimal amount;
	public final String reason;

	public PaymentFailedEvent(Long orderid, Long cardid, BigDecimal amount, String reason) {
		super(EventType.PAYMENT_FAILED);
		this.orderid = orderid;
		this.cardid = cardid;
		this.amount = amount;
		this.reason = reason;
	}

}
