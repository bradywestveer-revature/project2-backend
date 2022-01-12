package com.revature.project2backend.controllers;

import com.revature.project2backend.services.PasswordResetService;
import com.revature.project2backend.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.fail;

public class PasswordResetControllerTest {
	private final PasswordResetController passwordResetController;
	private final UserService userService = Mockito.mock (UserService.class);
	private final PasswordResetService passwordResetService = Mockito.mock (PasswordResetService.class);
	
	public PasswordResetControllerTest () {
		this.passwordResetController = new PasswordResetController (userService, passwordResetService);
	}
	
	@Test
	void resetPasswordWhenEmailIsNull () {
		fail ();
		
//		assert throws InvalidValueException and assert message
//		verify that sendPasswordResetEmail was never called
	}
	
	@Test
	void resetPasswordWhenUserWithEmailDoesNotExist () {
		fail ();
		
//		assert throws InvalidValueException and assert message
//		verify that sendPasswordResetEmail was never called
	}
	
	@Test
	void resetPassword () {
		fail ();
		
//		verify that sendPasswordResetEmail was called
//		assertEquals (expectedResult, method call)
	}
	
	@Test
	void changePasswordWhenTokenIsNull () {
		fail ();
		
//		assert throws InvalidValueException and assert message
//		verify that changePasswordAndDeleteToken was never called
	}
	
	@Test
	void changePasswordWhenPasswordIsNull () {
		fail ();
		
//		assert throws InvalidValueException and assert message
//		verify that changePasswordAndDeleteToken was never called
	}
	
	@Test
	void changePasswordWhenTokenIsNotOwnedByAUser () {
		fail ();
		
//		assert throws InvalidValueException and assert message
//		verify that changePasswordAndDeleteToken was not called
	}
	
	@Test
	void changePassword () {
		fail ();
		
//		verify that changePasswordAndDeleteToken was called
//		assertEquals (expectedResult, method call)
	}
}
