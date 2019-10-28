package com.rumanski.payment.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Refund {

	@Id
	@SequenceGenerator(name = "refund_id_seq", sequenceName = "refund_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refund_id_seq")
	Long id;

	Long paymentid;

	boolean refunded;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPaymentid() {
		return paymentid;
	}

	public void setPaymentid(Long paymentid) {
		this.paymentid = paymentid;
	}

	public boolean isRefunded() {
		return refunded;
	}

	public void setRefunded(boolean refunded) {
		this.refunded = refunded;
	}

}
