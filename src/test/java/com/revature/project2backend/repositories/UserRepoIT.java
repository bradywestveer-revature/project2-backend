package com.revature.project2backend.repositories;

import com.revature.project2backend.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepoIT {
	@Autowired
	UserRepo userRepo;
	
	@BeforeEach
	void setUp () {
		userRepo.save (new User ("John", "Smith", "johnsmith@example.com", "johnsmith", "password"));
	}
	
	@AfterEach
	void tearDown () {
		userRepo.deleteAll ();
	}
	
	@Test
	void findByUsername () {
		User user = new User ("John", "Smith", "johnsmith@example.com", "johnsmith", "password");
		
		User result = userRepo.findByUsername (user.getUsername ());
		
		user.setId (result.getId ());
		
		assertEquals (user, result);
	}
	
	@Test
	void findByEmail () {
		User user = new User ("John", "Smith", "johnsmith@example.com", "johnsmith", "password");
		
		User result = userRepo.findByEmail (user.getEmail ());
		
		user.setId (result.getId ());
		
		assertEquals (user, result);
	}
	
	@Test
	void findByPasswordResetId () {
		
	}
	
	@Test
	void findByPasswordResetToken () {
		
	}
}