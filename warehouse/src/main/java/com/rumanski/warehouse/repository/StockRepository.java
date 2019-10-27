package com.rumanski.warehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rumanski.warehouse.model.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

	List<Stock> findByProductid(Long productid);
}
