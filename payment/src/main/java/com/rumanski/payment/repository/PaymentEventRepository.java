package com.rumanski.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rumanski.payment.model.PaymentEvent;

@Repository
public interface PaymentEventRepository extends JpaRepository<PaymentEvent, Long> {

}
