package com.revature.project2backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.project2backend.services.PasswordResetService;
import com.revature.project2backend.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.fail;

@WebMvcTest (PasswordResetController.class)
public class PasswordResetControllerIT {
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private PasswordResetController passwordResetController;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private PasswordResetService passwordResetService;
	
	private final ObjectMapper json = new ObjectMapper ();
	
	//todo use other Controller IT tests as a template/reference
	
	@Test
	void resetPasswordWhenEmailIsNull () {
		fail ();
	}
	
	@Test
	void resetPasswordWhenUserWithEmailDoesNotExist () {
		fail ();
	}
	
	@Test
	void resetPassword () {
		fail ();
	}
	
	@Test
	void changePasswordWhenTokenIsNull () {
		fail ();
	}
	
	@Test
	void changePasswordWhenPasswordIsNull () {
		fail ();
	}
	
	@Test
	void changePasswordWhenTokenIsNotOwnedByAUser () {
		fail ();
	}
	
	@Test
	void changePassword () {
		fail ();
	}
}
