package com.revature.project2backend.repositories;

import com.revature.project2backend.models.PasswordReset;
import com.revature.project2backend.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepoIT {
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private PasswordResetRepo passwordResetRepo;
	
	@BeforeEach
	void setUp () {
		userRepo.save (new User ("John", "Smith", "johnsmith@example.com", "johnsmith", "password"));
		userRepo.save (new User ("Sarah", "Smith", "sarahsmith@example.com", "sarahsmith", "password"));
		userRepo.save (new User ("Tom", "Smith", "tomsmith@example.com", "tomsmith", "password"));
	}
	
	@AfterEach
	void tearDown () {
		passwordResetRepo.deleteAll();
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
	void findByPasswordResetToken () {
		User expectedUser = userRepo.findByUsername("sarahsmith");
		String token = UUID.randomUUID ().toString ();
		PasswordReset passwordReset = new PasswordReset(null, expectedUser, token);
		passwordResetRepo.save(passwordReset);
		User actualUser = userRepo.findByPasswordResetToken(token);
		assertEquals(expectedUser.getId(), actualUser.getId());
	}
}