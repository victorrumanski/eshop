package com.rumanski.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rumanski.onboarding.model.OnboardingEvent;

@Repository
public interface OnboardingEventRepository extends JpaRepository<OnboardingEvent, Long> {

}
