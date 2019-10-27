package com.rumanski.onboarding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rumanski.onboarding.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

	List<Address> findByUseridAndRemoved(Long userid, boolean removed);

}
