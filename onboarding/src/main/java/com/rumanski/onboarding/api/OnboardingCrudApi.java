package com.rumanski.onboarding.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rumanski.onboarding.BadRequestException;
import com.rumanski.onboarding.model.User;
import com.rumanski.onboarding.repository.UserRepository;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingCrudApi {

	@Autowired
	UserRepository userRepo;

	/**
	 * !!! THE BIG THING HERE IS THE ABSCENSE OF EVENTS !!!!
	 */
	@PutMapping("/users/changepassword")
	public ResponseEntity<User> changePassword(String currentPass, String password, String confirmPassword) {
		if (!password.equals(confirmPassword)) {
			new BadRequestException("Passwords don't match!");
		}

		Long loggedInUserId = 1l;// get it from spring security
		User user = userRepo.findById(loggedInUserId).get();
		user.setPassword(password);
		user = userRepo.save(user);
		return ResponseEntity.ok().body(user);
	}
}
