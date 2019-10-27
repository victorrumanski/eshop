package com.rumanski.basket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rumanski.basket.model.BasketItem;

@Repository
public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {

	List<BasketItem> findByUserid(Long userid);
	List<BasketItem> findByProductid(Long productid);
}
