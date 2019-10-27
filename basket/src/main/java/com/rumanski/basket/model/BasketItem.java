package com.rumanski.basket.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "basketitem")
public class BasketItem {

	@Id
	@SequenceGenerator(name = "basketitem_id_seq", sequenceName = "basketitem_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "basketitem_id_seq")
	Long id;

	Long userid;

	Long productid;

	BigDecimal price;

	BigDecimal quantity;

	@Transient
	List<PriceChangeAlert> priceChanges;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductid() {
		return productid;
	}

	public void setProductid(Long productid) {
		this.productid = productid;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public List<PriceChangeAlert> getPriceChanges() {
		return priceChanges;
	}

	public void setPriceChanges(List<PriceChangeAlert> priceChanges) {
		this.priceChanges = priceChanges;
	}

}
