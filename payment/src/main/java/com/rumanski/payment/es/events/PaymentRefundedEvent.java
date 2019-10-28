package com.rumanski.payment.es.events;

public class PaymentRefundedEvent extends PaymentDomainEvent {

	public final Long paymentid;

	public PaymentRefundedEvent(Long paymentid) {
		super(EventType.PAYMENT_REFUNDED);
		this.paymentid = paymentid;
	}

}
