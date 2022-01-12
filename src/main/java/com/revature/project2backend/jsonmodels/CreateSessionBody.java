package com.revature.project2backend.jsonmodels;

import lombok.Data;

/**
 * CreateSessionBody is used as the body for the method in SessionController that creates a session
 */
@Data
public class CreateSessionBody {
	/**
	 * The username or email to login with
	 */
	private String identifier;
	
	/**
	 * The password to login with
	 */
	private String password;
}
