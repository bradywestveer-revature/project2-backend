package com.revature.project2backend.controllers;

import com.revature.project2backend.exceptions.InvalidCredentialsException;
import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.jsonmodels.CreateSessionBody;
import com.revature.project2backend.jsonmodels.JsonResponse;
import com.revature.project2backend.models.User;
import com.revature.project2backend.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.jupiter.api.Assertions.*;

public class SessionControllerTest {
	private final SessionController sessionController;
	private final UserService userService = Mockito.mock (UserService.class);
	
	public SessionControllerTest () {
		this.sessionController = new SessionController (this.userService);
	}
	
	@Test
	void createSessionWhenIdentifierIsNull () {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		CreateSessionBody createSessionBody = new CreateSessionBody ();
		
		createSessionBody.setPassword ("password");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.sessionController.createSession (createSessionBody, mockHttpSession));
		
		assertEquals ("Error! Invalid credentials", exception.getMessage ());
		
		assertEquals (0, mockHttpSession.getValueNames ().length);
	}
	
	@Test
	void createSessionWhenPasswordIsNull () {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		CreateSessionBody createSessionBody = new CreateSessionBody ();
		
		createSessionBody.setIdentifier ("identifier");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.sessionController.createSession (createSessionBody, mockHttpSession));
		
		assertEquals ("Error! Invalid credentials", exception.getMessage ());
		
		assertEquals (0,mockHttpSession.getValueNames ().length);
	}
	
	@Test
	void createSession () throws InvalidValueException, InvalidCredentialsException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		User user = new User ();
		
		CreateSessionBody createSessionBody = new CreateSessionBody ();
		
		createSessionBody.setIdentifier ("identifier");
		createSessionBody.setPassword ("password");
		
		Mockito.when (userService.loginUser (createSessionBody.getIdentifier (), createSessionBody.getPassword ())).thenReturn (user);
		
		assertEquals (ResponseEntity.ok (new JsonResponse ("Logged in", true, user, "/")), this.sessionController.createSession (createSessionBody, mockHttpSession));
		
		assertEquals (user, mockHttpSession.getAttribute ("user"));
	}
	
	@Test
	void deleteSession () {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		assertEquals (ResponseEntity.ok (new JsonResponse ("Logged out", true, null, "/login")), this.sessionController.deleteSession (mockHttpSession));
		
		assertTrue (mockHttpSession.isInvalid ());
	}
}
