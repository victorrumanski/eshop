package com.rumanski.ordering;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class OrderItem {

	@Id
	@SequenceGenerator(name = "orderitem_id_seq", sequenceName = "orderitem_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderitem_id_seq")
	Long id;

	Long productid;

	BigDecimal quantity, price;

}
