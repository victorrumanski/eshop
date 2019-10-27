package com.rumanski.ordering.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "event")
public class OrderingEvent {

	@Id
	@SequenceGenerator(name = "event_id_seq", sequenceName = "event_id_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_id_seq")
	private Long id;

	public Date created;
	public String type;
	public String payload;

	private Long correlationid;

	public OrderingEvent() {
	}

	public OrderingEvent(String type, Date timestamp, String payload) {
		super();
		this.type = type;
		this.created = timestamp;
		this.payload = payload;
	}

	@Override
	public String toString() {
		return "OrderingEvent [id=" + id + ", created=" + created + ", type=" + type + ", correlationid=" + correlationid
				+ ", payload=" + payload + "]";
	}

	public Long getCorrelationid() {
		return correlationid;
	}

	public void setCorrelationid(Long correlationid) {
		this.correlationid = correlationid;
	}

}
