package com.revature.project2backend.controllers;

import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.exceptions.NotFoundException;
import com.revature.project2backend.jsonmodels.ChangePasswordBody;
import com.revature.project2backend.jsonmodels.JsonResponse;
import com.revature.project2backend.jsonmodels.PasswordResetBody;
import com.revature.project2backend.models.User;
import com.revature.project2backend.services.PasswordResetService;
import com.revature.project2backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The PasswordResetController is responsible for mapping all endpoints necessary for passing data, pertaining to resetting a password, from
 * the client to the server.
 */
@RestController
@RequestMapping ("reset-password")
@CrossOrigin (origins = "${PROJECT2_FRONTEND_URL}", allowCredentials = "true")
public class PasswordResetController {

	/**
	 * An instance of UserService used for accessing the methods in the class.
	 */
	private final UserService userService;

	/**
	 * An instance of PasswordService used for accessing the methods in the class.
	 */
	private final PasswordResetService passwordResetService;

	/**
	 * This Constructor initializes UserService and PasswordReset service.
	 *
	 * @param userService An instance of UserService used for accessing the methods in the class.
	 * @param passwordResetService An instance of PasswordService used for accessing the methods in the class.
	 */
	@Autowired
	public PasswordResetController (UserService userService, PasswordResetService passwordResetService) {
		this.userService = userService;
		this.passwordResetService = passwordResetService;
	}

	/**
	 * Sends an email to the user with a password reset link.
	 *
	 * @param body A DTO containing an email
	 * @return A JsonResponse Object notifying the client of success
	 * @throws InvalidValueException Thrown when an invalid email is passed in.
	 */
	@PostMapping
	ResponseEntity <JsonResponse> resetPassword (@RequestBody PasswordResetBody body) throws InvalidValueException {
		if (body.getEmail () == null) {
			throw new InvalidValueException ("Invalid email");
		}
		
		User user = userService.getUserByEmail (body.getEmail ());
		
		if (user == null) {
			throw new InvalidValueException ("Email for this user does not exist in system.");
		}
		
		//Create password reset token for user and e-mail them the reset link
		passwordResetService.sendPasswordResetEmail (user, passwordResetService.createOrUpdateToken (user));
		
		return ResponseEntity.ok (new JsonResponse ("Successfully sent password reset e-mail to " + body.getEmail () + ".", true));
	}

	/**
	 * Changes and encrypts password after checking token validity and then deletes token.
	 *
	 * @param body A DTO containing an email and validating token
	 * @return A JsonResponse Object notifying the user that password was changed
	 * @throws InvalidValueException Thrown when token has expired or password is incorrect
	 * @throws NotFoundException Thrown when no user is found.
	 */
	@PutMapping
	ResponseEntity <JsonResponse> changePassword (@RequestBody ChangePasswordBody body) throws InvalidValueException, NotFoundException {
		if (body.getToken () == null) {
			throw new InvalidValueException ("Invalid password reset token");
		}
		
		if (body.getPassword () == null) {
			throw new InvalidValueException ("Invalid password");
		}
		
		User user = passwordResetService.getUserByPasswordResetToken (body.getToken ());
		
		if (user == null) {
			throw new InvalidValueException ("Password reset token invalid or has expired.");
		}
		
		passwordResetService.changePasswordAndDeleteToken (user, body.getPassword ());
		
		return ResponseEntity.ok (new JsonResponse ("Successfully set new password for " + user.getEmail () + ".", true, null, "login"));
	}
}
