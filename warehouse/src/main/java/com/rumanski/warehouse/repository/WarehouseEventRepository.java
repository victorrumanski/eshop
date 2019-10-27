package com.rumanski.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rumanski.warehouse.model.WarehouseEvent;

@Repository
public interface WarehouseEventRepository extends JpaRepository<WarehouseEvent, Long> {

}
