package com.rumanski.ordering;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Buyer {

	@Id
	@SequenceGenerator(name = "buyer_id_seq", sequenceName = "buyer_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "buyer_id_seq")
	Long id;

	Long userid;

	String name, email;

}
