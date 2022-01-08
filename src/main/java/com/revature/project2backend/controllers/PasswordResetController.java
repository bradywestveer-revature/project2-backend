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

@RestController
@RequestMapping ("reset-password")
@CrossOrigin (origins = "${PROJECT2_FRONTEND_URL}", allowCredentials = "true")
public class PasswordResetController {
	private final UserService userService;
	private final PasswordResetService passwordResetService;
	
	@Autowired
	public PasswordResetController (UserService userService, PasswordResetService passwordResetService) {
		this.userService = userService;
		this.passwordResetService = passwordResetService;
	}
	
	@PostMapping
	ResponseEntity <JsonResponse> resetPassword (@RequestBody PasswordResetBody body) throws InvalidValueException {
		User user = userService.getUserByEmail (body.getEmail ());
		if (user == null) {
			throw new InvalidValueException ("Email for this user does not exist in system.");
		}
		//Create password reset token for user and e-mail them the reset link
		passwordResetService.sendPasswordResetEmail (user, passwordResetService.createOrUpdateToken (user));
		return ResponseEntity.ok (new JsonResponse ("Successfully sent password reset e-mail to " + body.getEmail () + ".", true));
	}
	
	@PutMapping
	ResponseEntity <JsonResponse> changePassword (@RequestBody ChangePasswordBody body) throws InvalidValueException, NotFoundException {
		if (body.getToken () == null) {
			throw new InvalidValueException ("Invalid password reset token");
		}
		
		User user = passwordResetService.getUserByPasswordResetToken (body.getToken ());
		
		if (user == null) {
			throw new InvalidValueException ("Password reset token invalid or has expired.");
		}
		
		passwordResetService.changePasswordAndDeleteToken (user, body.getPassword ());
		
		return ResponseEntity.ok (new JsonResponse ("Successfully set new password for " + user.getEmail () + ".", true, null, "login"));
	}
}
