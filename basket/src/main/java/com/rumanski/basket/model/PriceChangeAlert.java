package com.rumanski.basket.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "pricechangealert")
public class PriceChangeAlert {

	@Id
	@SequenceGenerator(name = "pricechangealert_id_seq", sequenceName = "pricechangealert_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pricechangealert_id_seq")
	Long id;
	Date created;

	Long basketitemid;
	BigDecimal oldprice;
	BigDecimal newprice;

	public PriceChangeAlert() {
	}

	public PriceChangeAlert(Long basketitemid, BigDecimal oldprice, BigDecimal newprice) {
		super();
		this.basketitemid = basketitemid;
		this.oldprice = oldprice;
		this.newprice = newprice;
		this.created = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Long getBasketitemid() {
		return basketitemid;
	}

	public void setBasketitemid(Long basketitemid) {
		this.basketitemid = basketitemid;
	}

	public BigDecimal getOldprice() {
		return oldprice;
	}

	public void setOldprice(BigDecimal oldprice) {
		this.oldprice = oldprice;
	}

	public BigDecimal getNewprice() {
		return newprice;
	}

	public void setNewprice(BigDecimal newprice) {
		this.newprice = newprice;
	}

}
