package com.revature.project2backend.jsonmodels;

import lombok.Data;

import java.util.Map;

/**
 * UpdateUserBody is used as the body for the method in UserController that updates a user
 */
@Data
public class UpdateUserBody {
	/**
	 * The first name to change the user's first name to
	 */
	private String firstName;
	
	/**
	 * The last name to change the user's last name to
	 */
	private String lastName;
	
	/**
	 * The email to change the user's email to
	 */
	private String email;
	
	/**
	 * The username to change the user's username to
	 */
	private String username;
	
	/**
	 * The password to change the user's password to
	 */
	private String password;
	
	/**
	 * The profileImageData to use to set the user's profile image
	 */
	private Map <String, String> profileImageData;
}
