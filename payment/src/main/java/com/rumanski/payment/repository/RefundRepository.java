package com.rumanski.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rumanski.payment.model.Refund;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {

}
