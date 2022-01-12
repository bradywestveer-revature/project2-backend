package com.revature.project2backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.jsonmodels.CreateSessionBody;
import com.revature.project2backend.jsonmodels.JsonResponse;
import com.revature.project2backend.models.User;
import com.revature.project2backend.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest (SessionController.class)
public class SessionControllerIT {
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userService;
	
	private final ObjectMapper json = new ObjectMapper ();
	
	@Test
	void createSessionWhenIdentifierIsNull () throws Exception {
		CreateSessionBody createSessionBody = new CreateSessionBody ();
		
		createSessionBody.setPassword ("password");
		
		mvc.perform (MockMvcRequestBuilders.post ("/session")
			.contentType (MediaType.APPLICATION_JSON)
			.session (new MockHttpSession ())
			.content (json.writeValueAsString (createSessionBody)))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid credentials")))));
	}
	
	@Test
	void createSessionWhenPasswordIsNull () throws Exception {
		CreateSessionBody createSessionBody = new CreateSessionBody ();
		
		createSessionBody.setIdentifier ("identifier");
		
		mvc.perform (MockMvcRequestBuilders.post ("/session")
			.contentType (MediaType.APPLICATION_JSON)
			.session (new MockHttpSession ())
			.content (json.writeValueAsString (createSessionBody)))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid credentials")))));
	}
	
	@Test
	void createSession () throws Exception {
		User user = new User ();
		
		CreateSessionBody createSessionBody = new CreateSessionBody ();
		
		createSessionBody.setIdentifier ("identifier");
		createSessionBody.setPassword ("password");
		
		Mockito.when (userService.loginUser (createSessionBody.getIdentifier (), createSessionBody.getPassword ())).thenReturn (user);
		
		mvc.perform (MockMvcRequestBuilders.post ("/session")
			.contentType (MediaType.APPLICATION_JSON)
			.session (new MockHttpSession ())
			.content (json.writeValueAsString (createSessionBody)))
			
			.andExpect (MockMvcResultMatchers.status ().isOk ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse ("Logged in", true, user, "/"))));
	}
	
	@Test
	void deleteSession () throws Exception {
		CreateSessionBody createSessionBody = new CreateSessionBody ();
		
		createSessionBody.setIdentifier ("identifier");
		createSessionBody.setPassword ("password");
		
		mvc.perform (MockMvcRequestBuilders.delete ("/session")
				.contentType (MediaType.APPLICATION_JSON)
				.session (new MockHttpSession ())
				.content (json.writeValueAsString (createSessionBody)))
			
			.andExpect (MockMvcResultMatchers.status ().isOk ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse ("Logged out", true, null, "/login"))));
	}
}
