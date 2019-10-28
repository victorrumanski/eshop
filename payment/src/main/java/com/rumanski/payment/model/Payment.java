package com.rumanski.payment.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Payment {

	@Id
	@SequenceGenerator(name = "payment_id_seq", sequenceName = "payment_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_id_seq")
	Long id;

	Long orderid;

	Long cardid;

	BigDecimal amount;

	Date lasttry, paid, failed, refunded;

	PaymentStatus status;

	public static enum PaymentStatus {

		PAYMENT_REQUESTED,

		PAYMENT_COMPLETED,

		PAYMENT_FAILED, // when a payment fails is the same as canceled

		REFUNDED

		;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderid() {
		return orderid;
	}

	public void setOrderid(Long orderid) {
		this.orderid = orderid;
	}

	public Long getCardid() {
		return cardid;
	}

	public void setCardid(Long cardid) {
		this.cardid = cardid;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getLasttry() {
		return lasttry;
	}

	public void setLasttry(Date lasttry) {
		this.lasttry = lasttry;
	}

	public Date getPaid() {
		return paid;
	}

	public void setPaid(Date paid) {
		this.paid = paid;
	}

	public Date getFailed() {
		return failed;
	}

	public void setFailed(Date failed) {
		this.failed = failed;
	}

	public Date getRefunded() {
		return refunded;
	}

	public void setRefunded(Date refunded) {
		this.refunded = refunded;
	}

	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

}
