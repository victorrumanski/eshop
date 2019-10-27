package com.rumanski.warehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rumanski.warehouse.model.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	List<Reservation> findByOrderitemid(Long orderitemid);

	List<Reservation> findByStockid(Long stockid);
}
