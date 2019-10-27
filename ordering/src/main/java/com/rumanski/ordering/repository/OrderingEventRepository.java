package com.rumanski.ordering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rumanski.ordering.model.OrderingEvent;

@Repository
public interface OrderingEventRepository extends JpaRepository<OrderingEvent, Long> {

}
