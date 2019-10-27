package com.rumanski.warehouse.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Reservation {

	@Id
	@SequenceGenerator(name = "reservation_id_seq", sequenceName = "reservation_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_id_seq")
	Long id;

	Long orderitemid;

	Long stockid;

	BigDecimal quantity;

	ReservationStatus status;

	public static enum ReservationStatus {

		RESERVED, PICKED,

		CANCELED;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderitemid() {
		return orderitemid;
	}

	public void setOrderitemid(Long orderitemid) {
		this.orderitemid = orderitemid;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public ReservationStatus getStatus() {
		return status;
	}

	public void setStatus(ReservationStatus status) {
		this.status = status;
	}

	public Long getStockid() {
		return stockid;
	}

	public void setStockid(Long stockid) {
		this.stockid = stockid;
	}

}
