package com.rumanski.ordering;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

public class Order {

	@Id
	@SequenceGenerator(name = "order_id_seq", sequenceName = "order_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_seq")
	Long id;

	Date created;

	Long userid;

	String street, city, state, postalcode;
	String ccnumber, ccname, ccvaliduntil, ccverificationcode;

	OrderStatus status;

}