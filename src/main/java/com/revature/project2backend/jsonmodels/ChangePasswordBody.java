package com.revature.project2backend.jsonmodels;

import lombok.Data;

/**
 * ChangePasswordBody is used as the body for the method in PasswordResetController that changes a user's password
 */
@Data
public class ChangePasswordBody {   // Change the password using our generated token based on Forgot Password screen
	/**
	 * The password reset token
	 */
	String token;
	
	/**
	 * The new password
	 */
    String password;
}
