package com.rumanski.basket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rumanski.basket.model.PriceChangeAlert;

@Repository
public interface PriceChangeAlertRepository extends JpaRepository<PriceChangeAlert, Long> {

	List<PriceChangeAlert> findByBasketitemid(Long basketitemid);
}
