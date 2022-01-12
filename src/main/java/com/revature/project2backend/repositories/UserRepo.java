package com.revature.project2backend.repositories;

import com.revature.project2backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository <User, Integer> {

	/**
	 * Custom method for finding a User by a username.
	 *
	 * @param username A unique String associated with a User
	 * @return A User Object with a specific username
	 */
	User findByUsername (String username);

	/**
	 * Custom method for finding a User by an email.
	 *
	 * @param email A unique String associated with a User
	 * @return A User Object with a specific email
	 */
	User findByEmail (String email);

	/**
	 * Custom method for finding a User by a token.
	 *
	 * @param token A unique String associated with a User
	 * @return A User Object with a specific token
	 */
	User findByPasswordResetToken(String token);
}
