package com.rumanski.onboarding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rumanski.onboarding.model.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
	List<Card> findByUserid(Long userid);

}
