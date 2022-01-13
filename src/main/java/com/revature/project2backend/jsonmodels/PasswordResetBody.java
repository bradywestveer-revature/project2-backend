package com.revature.project2backend.jsonmodels;

import lombok.Data;

/**
 * PasswordResetBody is used as the body for the method in PasswordResetController that starts the process of resetting a user's password
 */
@Data
public class PasswordResetBody {
	/**
	 * The email of the user whose password should be reset. Also, the email to send the password reset token to
	 */
    String email;
}
