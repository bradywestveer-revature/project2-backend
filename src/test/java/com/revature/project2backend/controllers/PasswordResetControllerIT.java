package com.revature.project2backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.jsonmodels.ChangePasswordBody;
import com.revature.project2backend.jsonmodels.JsonResponse;
import com.revature.project2backend.jsonmodels.PasswordResetBody;
import com.revature.project2backend.models.PasswordReset;
import com.revature.project2backend.models.User;
import com.revature.project2backend.services.PasswordResetService;
import com.revature.project2backend.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

@WebMvcTest (PasswordResetController.class)
public class PasswordResetControllerIT {
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private PasswordResetService passwordResetService;
	
	private final ObjectMapper json = new ObjectMapper ();

	@Test
	void resetPasswordWhenEmailIsNull () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/reset-password")
						.contentType (MediaType.APPLICATION_JSON)
						.content (json.writeValueAsString (new PasswordResetBody())))	// email defaults to null since not assigned
				.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
				.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse(new InvalidValueException("Invalid email")))));
	}
	
	@Test
	void resetPasswordWhenUserWithEmailDoesNotExist () throws Exception {
		PasswordResetBody body = new PasswordResetBody();
		body.setEmail("usernotfound@javadev.com");
		Mockito.when(userService.getUserByEmail (body.getEmail ())).thenReturn(null);
		mvc.perform (MockMvcRequestBuilders.post ("/reset-password")
						.contentType (MediaType.APPLICATION_JSON)
						.content (json.writeValueAsString (body) ))
				.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
				.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse(new InvalidValueException("Email for this user does not exist in system.")))));
	}
	
	@Test
	void resetPassword () throws Exception {
		PasswordResetBody body = new PasswordResetBody();
		body.setEmail("valid@javadev.com");
		User user = new User(1, "John", "Smith", body.getEmail(), "johnsmith", "password", "", null);
		Mockito.when(userService.getUserByEmail (body.getEmail ())).thenReturn(user);
		String token = UUID.randomUUID ().toString ();
		PasswordReset passwordReset = new PasswordReset(1, user, token);
		Mockito.when(passwordResetService.createOrUpdateToken (user)).thenReturn(passwordReset);
		String jsonResponse = json.writeValueAsString (new JsonResponse ("Successfully sent password reset e-mail to " + body.getEmail () + ".", true));
		mvc.perform (MockMvcRequestBuilders.post ("/reset-password")
						.contentType (MediaType.APPLICATION_JSON)
						.content (json.writeValueAsString (body) ))
				.andExpect (MockMvcResultMatchers.status ().isOk ())
				.andExpect (MockMvcResultMatchers.content ().json (jsonResponse));
		Mockito.verify(this.passwordResetService, Mockito.times(1)).sendPasswordResetEmail (user, passwordResetService.createOrUpdateToken (user));
	}
	
	@Test
	void changePasswordWhenTokenIsNull () throws Exception {
		ChangePasswordBody body = new ChangePasswordBody();
		body.setPassword("pass123");
		body.setToken(null);
		mvc.perform (MockMvcRequestBuilders.put ("/reset-password")
						.contentType (MediaType.APPLICATION_JSON)
						.content (json.writeValueAsString (body)))
				.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
				.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse(new InvalidValueException("Invalid password reset token")))));
	}
	
	@Test
	void changePasswordWhenPasswordIsNull () throws Exception {
		ChangePasswordBody body = new ChangePasswordBody();
		body.setPassword(null);
		body.setToken(UUID.randomUUID ().toString ());
		mvc.perform (MockMvcRequestBuilders.put ("/reset-password")
						.contentType (MediaType.APPLICATION_JSON)
						.content (json.writeValueAsString (body)))
				.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
				.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse(new InvalidValueException("Invalid password")))));
	}
	
	@Test
	void changePasswordWhenTokenIsNotOwnedByAUser () throws Exception {
		ChangePasswordBody body = new ChangePasswordBody();
		body.setPassword("password");
		body.setToken(UUID.randomUUID ().toString ());
		Mockito.when(passwordResetService.getUserByPasswordResetToken (body.getToken ())).thenReturn(null); // No user found
		mvc.perform (MockMvcRequestBuilders.put ("/reset-password")
						.contentType (MediaType.APPLICATION_JSON)
						.content (json.writeValueAsString (body)))
				.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
				.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse(new InvalidValueException("Password reset token invalid or has expired.")))));
	}
	
	@Test
	void changePassword () throws Exception{
		ChangePasswordBody body = new ChangePasswordBody();
		body.setPassword("password");
		String token=UUID.randomUUID ().toString ();
		body.setToken(token);
		User user = new User(1, "John", "Smith", "johnsmith@javadev.com", "johnsmith", "password", "", null);
		PasswordReset passwordReset = new PasswordReset(1, user, token);
		user.setPasswordReset(passwordReset);
		Mockito.when(passwordResetService.getUserByPasswordResetToken (body.getToken ())).thenReturn(user);
		String jsonResponse = json.writeValueAsString (new JsonResponse ("Successfully set new password for " + user.getEmail () + ".", true, null, "login"));
		mvc.perform (MockMvcRequestBuilders.put ("/reset-password")
						.contentType (MediaType.APPLICATION_JSON)
						.content (json.writeValueAsString (body) ))
				.andExpect (MockMvcResultMatchers.status ().isOk ())
				.andExpect (MockMvcResultMatchers.content ().json (jsonResponse));
		Mockito.verify(this.passwordResetService, Mockito.times(1)).changePasswordAndDeleteToken (user, body.getPassword ());
	}
}
