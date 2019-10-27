package com.rumanski.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rumanski.onboarding.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
