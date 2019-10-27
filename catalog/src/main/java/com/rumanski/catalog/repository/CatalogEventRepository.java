package com.rumanski.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rumanski.catalog.model.CatalogEvent;

@Repository
public interface CatalogEventRepository extends JpaRepository<CatalogEvent, Long> {

}
