package com.revature.project2backend.controllers;

import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.exceptions.NotFoundException;
import com.revature.project2backend.jsonmodels.ChangePasswordBody;
import com.revature.project2backend.jsonmodels.JsonResponse;
import com.revature.project2backend.jsonmodels.PasswordResetBody;
import com.revature.project2backend.models.PasswordReset;
import com.revature.project2backend.models.User;
import com.revature.project2backend.services.PasswordResetService;
import com.revature.project2backend.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class PasswordResetControllerTest {
	private final PasswordResetController passwordResetController;
	private final UserService userService = Mockito.mock (UserService.class);
	private final PasswordResetService passwordResetService = Mockito.mock (PasswordResetService.class);
	
	public PasswordResetControllerTest () {
		this.passwordResetController = new PasswordResetController (userService, passwordResetService);
	}
	
	@Test
	void resetPasswordWhenEmailIsNull () throws Exception {
		PasswordResetBody body = new PasswordResetBody();
		body.setEmail(null);
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.passwordResetController.resetPassword (body));
		assertEquals("Error! Invalid email", exception.getMessage());
		Mockito.verify(userService, Mockito.never()).getUserByEmail (Mockito.any ());
		Mockito.verify(passwordResetService, Mockito.never()).createOrUpdateToken (Mockito.any());
		Mockito.verify(passwordResetService, Mockito.never()).sendPasswordResetEmail (Mockito.any(), Mockito.any());
	}
	
	@Test
	void resetPasswordWhenUserWithEmailDoesNotExist () throws Exception {
		PasswordResetBody body = new PasswordResetBody();
		body.setEmail("johnsmith@javadev.com");
		Mockito.when(userService.getUserByEmail (body.getEmail ())).thenReturn(null);
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.passwordResetController.resetPassword (body));
		assertEquals("Error! Email for this user does not exist in system.", exception.getMessage());
		Mockito.verify(passwordResetService, Mockito.never()).createOrUpdateToken (Mockito.any());
		Mockito.verify(passwordResetService, Mockito.never()).sendPasswordResetEmail (Mockito.any(), Mockito.any());
	}
	
	@Test
	void resetPassword () throws Exception{
		PasswordResetBody body = new PasswordResetBody();
		body.setEmail("johnsmith@javadev.com");
		User user = new User(1, "John", "Smith", body.getEmail(), "johnsmith", "password", "", null);
		Mockito.when(userService.getUserByEmail (body.getEmail ())).thenReturn(user);
		String token = UUID.randomUUID ().toString ();
		PasswordReset passwordReset = new PasswordReset(1, user, token);
		Mockito.when(passwordResetService.createOrUpdateToken (user)).thenReturn(passwordReset);
		assertEquals (ResponseEntity.ok (new JsonResponse("Successfully sent password reset e-mail to " + body.getEmail () + ".", true)),
				passwordResetController.resetPassword(body));
		Mockito.verify(this.passwordResetService, Mockito.times(1)).sendPasswordResetEmail (user, passwordResetService.createOrUpdateToken (user));
	}
	
	@Test
	void changePasswordWhenTokenIsNull () throws Exception {
		ChangePasswordBody body = new ChangePasswordBody();
		body.setPassword("password");
		body.setToken(null);

		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.passwordResetController.changePassword (body));
		assertEquals("Error! Invalid password reset token", exception.getMessage());
		Mockito.verify(passwordResetService, Mockito.never()).getUserByPasswordResetToken (Mockito.any());
		Mockito.verify(passwordResetService, Mockito.never()).changePasswordAndDeleteToken (Mockito.any(), Mockito.any());
	}
	
	@Test
	void changePasswordWhenPasswordIsNull () throws Exception {
		ChangePasswordBody body = new ChangePasswordBody();
		body.setPassword(null);
		String token = UUID.randomUUID ().toString ();
		body.setToken(token);
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.passwordResetController.changePassword (body));
		assertEquals("Error! Invalid password", exception.getMessage());
		Mockito.verify(passwordResetService, Mockito.never()).getUserByPasswordResetToken (Mockito.any());
		Mockito.verify(passwordResetService, Mockito.never()).changePasswordAndDeleteToken (Mockito.any(), Mockito.any());
	}
	
	@Test
	void changePasswordWhenTokenIsNotOwnedByAUser () throws Exception {
		ChangePasswordBody body = new ChangePasswordBody();
		body.setPassword("password");
		String token = UUID.randomUUID ().toString ();
		body.setToken(token);
		Mockito.when(passwordResetService.getUserByPasswordResetToken (body.getToken ())).thenReturn(null); // No user found
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.passwordResetController.changePassword (body));
		assertEquals("Error! Password reset token invalid or has expired.", exception.getMessage());
		Mockito.verify(passwordResetService, Mockito.never()).changePasswordAndDeleteToken (Mockito.any(), Mockito.any());
	}
	
	@Test
	void changePassword () throws InvalidValueException, NotFoundException {
		ChangePasswordBody body = new ChangePasswordBody();
		body.setPassword("password");
		String token=UUID.randomUUID ().toString ();
		body.setToken(token);
		User user = new User(1, "John", "Smith", "johnsmith@javadev.com", "johnsmith", "password", "", null);
		PasswordReset passwordReset = new PasswordReset(1, user, token);
		user.setPasswordReset(passwordReset);
		Mockito.when(passwordResetService.getUserByPasswordResetToken (body.getToken ())).thenReturn(user);
		assertEquals(ResponseEntity.ok (new JsonResponse ("Successfully set new password for " + user.getEmail () + ".", true, null, "login")),
				this.passwordResetController.changePassword(body));
		Mockito.verify(this.passwordResetService, Mockito.times(1)).changePasswordAndDeleteToken (user, body.getPassword ());
	}
}
