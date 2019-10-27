package com.rumanski.basket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rumanski.basket.model.BasketEvent;

@Repository
public interface BasketEventRepository extends JpaRepository<BasketEvent, Long> {

}
