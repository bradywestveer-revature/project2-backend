package com.revature.project2backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.exceptions.UnauthorizedException;
import com.revature.project2backend.jsonmodels.JsonResponse;
import com.revature.project2backend.jsonmodels.UpdateUserBody;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebMvcTest (UserController.class)
public class UserControllerIT {
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userService;
	
	private final ObjectMapper json = new ObjectMapper ();
	
	//we are passing a string directly because we can't json.write a User object because the password won't get written due to the @JsonAccess on the password field in User
	
	@Test
	void createUserWhenFirstNameIsNull () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.content ("{\"lastName\": \"Smith\",\"email\": \"johnsmith@example.com\",\"username\": \"johnsmith\",\"password\": \"password\"}"))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void createUserWhenLastNameIsNull () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.content ("{\"firstName\": \"John\",\"email\": \"johnsmith@example.com\",\"username\": \"johnsmith\",\"password\": \"password\"}"))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void createUserWhenEmailIsNull () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.content ("{\"firstName\": \"John\",\"lastName\": \"Smith\",\"username\": \"johnsmith\",\"password\": \"password\"}"))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void createUserWhenUsernameIsNull () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.content ("{\"firstName\": \"John\",\"lastName\": \"Smith\",\"email\": \"johnsmith@example.com\",\"password\": \"password\"}"))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void createUserWhenPasswordIsNull () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.content ("{\"firstName\": \"John\",\"lastName\": \"Smith\",\"email\": \"johnsmith@example.com\",\"username\": \"johnsmith\"}"))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void createUserWhenFirstNameIsEmpty () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.content ("{\"firstName\": \"\",\"lastName\": \"Smith\",\"email\": \"johnsmith@example.com\",\"username\": \"johnsmith\",\"password\": \"password\"}"))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void createUserWhenLastNameIsEmpty () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.content ("{\"firstName\": \"John\",\"lastName\": \"\",\"email\": \"johnsmith@example.com\",\"username\": \"johnsmith\",\"password\": \"password\"}"))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void createUserWhenEmailIsEmpty () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.content ("{\"firstName\": \"John\",\"lastName\": \"Smith\",\"email\": \"\",\"username\": \"johnsmith\",\"password\": \"password\"}"))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void createUserWhenUsernameIsEmpty () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.content ("{\"firstName\": \"John\",\"lastName\": \"Smith\",\"email\": \"johnsmith@example.com\",\"username\": \"\",\"password\": \"password\"}"))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void createUserWhenPasswordIsEmpty () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.content ("{\"firstName\": \"John\",\"lastName\": \"Smith\",\"email\": \"johnsmith@example.com\",\"username\": \"johnsmith\",\"password\": \"\"}"))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void createUserWhenFirstNameIsWhitespace () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.content ("{\"firstName\": \"           \",\"lastName\": \"Smith\",\"email\": \"johnsmith@example.com\",\"username\": \"johnsmith\",\"password\": \"password\"}"))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void createUserWhenLastNameIsWhitespace () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.content ("{\"firstName\": \"John\",\"lastName\": \"               \",\"email\": \"johnsmith@example.com\",\"username\": \"johnsmith\",\"password\": \"password\"}"))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void createUserWhenEmailIsWhitespace () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.content ("{\"firstName\": \"John\",\"lastName\": \"Smith\",\"email\": \"          \",\"username\": \"johnsmith\",\"password\": \"password\"}"))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void createUserWhenUsernameIsWhitespace () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.content ("{\"firstName\": \"John\",\"lastName\": \"Smith\",\"email\": \"johnsmith@example.com\",\"username\": \"         \",\"password\": \"password\"}"))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void createUserWhenPasswordIsWhitespace () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.content ("{\"firstName\": \"John\",\"lastName\": \"Smith\",\"email\": \"johnsmith@example.com\",\"username\": \"johnsmith\",\"password\": \"        \"}"))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void createUserWhenUsernameIsTaken () throws Exception {
		User user = new User ();
		
		user.setId (2);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		
		Mockito.when (userService.getUserByUsername (user.getUsername ())).thenReturn (user);
		
		mvc.perform (MockMvcRequestBuilders.post ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.content ("{\"firstName\": \"John\",\"lastName\": \"Smith\",\"email\": \"johnsmith@example.com\",\"username\": \"johnsmith\",\"password\": \"password\"}"))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Username already in use")))));
	}
	
	@Test
	void createUserWhenEmailIsTaken () throws Exception {
		User user = new User ();
		
		user.setId (2);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		
		Mockito.when (userService.getUserByEmail (user.getEmail ())).thenReturn (user);
		
		mvc.perform (MockMvcRequestBuilders.post ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.content ("{\"firstName\": \"John\",\"lastName\": \"Smith\",\"email\": \"johnsmith@example.com\",\"username\": \"johnsmith\",\"password\": \"password\"}"))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Email already in use")))));
	}
	
	@Test
	void createUserWhenUsernameIsInvalid () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.content ("{\"firstName\": \"John\",\"lastName\": \"Smith\",\"email\": \"johnsmith@example.com\",\"username\": \"@.-/\",\"password\": \"password\"}"))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid username")))));
	}
	
	@Test
	void createUserWhenEmailIsInvalid () throws Exception {
		mvc.perform (MockMvcRequestBuilders.post ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.content ("{\"firstName\": \"John\",\"lastName\": \"Smith\",\"email\": \"not valid email\",\"username\": \"johnsmith\",\"password\": \"password\"}"))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid email")))));
	}
	
	@Test
	void createUser () throws Exception {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		mvc.perform (MockMvcRequestBuilders.post ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.content ("{\"firstName\": \"John\",\"lastName\": \"Smith\",\"email\": \"johnsmith@example.com\",\"username\": \"johnsmith\",\"password\": \"password\"}"))
			
			.andExpect (MockMvcResultMatchers.status ().isOk ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse ("Created user", true, null, "/login"))));
	}
	
	@Test
	void getUsersWhenNotLoggedIn () throws Exception {
		mvc.perform (MockMvcRequestBuilders.get ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.session (new MockHttpSession ()))
			
			.andExpect (MockMvcResultMatchers.status ().isUnauthorized ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new UnauthorizedException (), "/login"))));
	}
	
	@Test
	void getUsers () throws Exception {
		User user1 = new User ();
		
		user1.setId (1);
		user1.setFirstName ("John");
		user1.setLastName ("Smith");
		user1.setEmail ("johnsmith@example.com");
		user1.setUsername ("johnsmith");
		user1.setPassword ("password");
		
		User user2 = new User ();
		
		user2.setId (2);
		user2.setFirstName ("John");
		user2.setLastName ("Smith");
		user2.setEmail ("johnsmith@example.com");
		user2.setUsername ("johnsmith");
		user2.setPassword ("password");
		
		User user3 = new User ();
		
		user3.setId (3);
		user3.setFirstName ("John");
		user3.setLastName ("Smith");
		user3.setEmail ("johnsmith@example.com");
		user3.setUsername ("johnsmith");
		user3.setPassword ("password");
		
		List <User> users = new ArrayList <> ();
		
		users.add (user1);
		users.add (user2);
		users.add (user3);
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		Mockito.when (userService.getUsers ()).thenReturn (users);
		
		mvc.perform (MockMvcRequestBuilders.get ("/user")
			.contentType (MediaType.APPLICATION_JSON)
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isOk ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse ("Found 3 users", true, users))));
	}
	
	@Test
	void getUserWhenNotLoggedIn () throws Exception {
		mvc.perform (MockMvcRequestBuilders.get ("/user/1")
			.contentType (MediaType.APPLICATION_JSON)
			.session (new MockHttpSession ()))
			
			.andExpect (MockMvcResultMatchers.status ().isUnauthorized ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new UnauthorizedException (), "/login"))));
	}
	
	@Test
	void getUser () throws Exception {
		int userId = 1;
		
		User user = new User ();
		
		user.setId (userId);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		Mockito.when (userService.getUser (userId)).thenReturn (user);
		
		mvc.perform (MockMvcRequestBuilders.get ("/user/" + userId)
			.contentType (MediaType.APPLICATION_JSON)
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isOk ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse ("Found user", true, user))));
	}
	
	@Test
	void updateUserWhenNotLoggedIn () throws Exception {
		mvc.perform (MockMvcRequestBuilders.put ("/user/1")
			.contentType (MediaType.APPLICATION_JSON)
			.session (new MockHttpSession ())
			.content (json.writeValueAsString (new UpdateUserBody ())))
			
			.andExpect (MockMvcResultMatchers.status ().isUnauthorized ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new UnauthorizedException (), "/login"))));
	}
	
	@Test
	void updateUserWhenIdDoesNotMatchUserId () throws Exception {
		User user = new User ();
		
		user.setId (1);
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user.getId () + 1)
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isUnauthorized ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new UnauthorizedException (), "/login"))));
	}
	
	@Test
	void updateUserWhenFirstNameIsNull () throws Exception {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setLastName (user.getLastName ());
		updateUserBody.setEmail (user.getEmail ());
		updateUserBody.setUsername (user.getUsername ());
		updateUserBody.setPassword (user.getPassword ());
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isOk ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse ("Updated user", true, user))));
	}
	
	@Test
	void updateUserWhenLastNameIsNull () throws Exception {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (user.getFirstName ());
		updateUserBody.setEmail (user.getEmail ());
		updateUserBody.setUsername (user.getUsername ());
		updateUserBody.setPassword (user.getPassword ());
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isOk ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse ("Updated user", true, user))));
	}
	
	@Test
	void updateUserWhenEmailIsNull () throws Exception {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (user.getFirstName ());
		updateUserBody.setLastName (user.getLastName ());
		updateUserBody.setUsername (user.getUsername ());
		updateUserBody.setPassword (user.getPassword ());
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isOk ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse ("Updated user", true, user))));
	}
	
	@Test
	void updateUserWhenUsernameIsNull () throws Exception {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (user.getFirstName ());
		updateUserBody.setLastName (user.getLastName ());
		updateUserBody.setEmail (user.getEmail ());
		updateUserBody.setPassword (user.getPassword ());
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isOk ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse ("Updated user", true, user))));
	}
	
	@Test
	void updateUserWhenPasswordIsNull () throws Exception {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (user.getFirstName ());
		updateUserBody.setLastName (user.getLastName ());
		updateUserBody.setEmail (user.getEmail ());
		updateUserBody.setUsername (user.getUsername ());
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isOk ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse ("Updated user", true, user))));
	}
	
	@Test
	void updateUserWhenFirstNameIsEmpty () throws Exception {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (user.getFirstName ());
		updateUserBody.setLastName (user.getLastName ());
		updateUserBody.setEmail (user.getEmail ());
		updateUserBody.setUsername (user.getUsername ());
		updateUserBody.setPassword (user.getPassword ());
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void updateUserWhenLastNameIsEmpty () throws Exception {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (user.getFirstName ());
		updateUserBody.setLastName (user.getLastName ());
		updateUserBody.setEmail (user.getEmail ());
		updateUserBody.setUsername (user.getUsername ());
		updateUserBody.setPassword (user.getPassword ());
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void updateUserWhenEmailIsEmpty () throws Exception {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (user.getFirstName ());
		updateUserBody.setLastName (user.getLastName ());
		updateUserBody.setEmail (user.getEmail ());
		updateUserBody.setUsername (user.getUsername ());
		updateUserBody.setPassword (user.getPassword ());
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void updateUserWhenUsernameIsEmpty () throws Exception {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("");
		user.setPassword ("password");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (user.getFirstName ());
		updateUserBody.setLastName (user.getLastName ());
		updateUserBody.setEmail (user.getEmail ());
		updateUserBody.setUsername (user.getUsername ());
		updateUserBody.setPassword (user.getPassword ());
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void updateUserWhenPasswordIsEmpty () throws Exception {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (user.getFirstName ());
		updateUserBody.setLastName (user.getLastName ());
		updateUserBody.setEmail (user.getEmail ());
		updateUserBody.setUsername (user.getUsername ());
		updateUserBody.setPassword (user.getPassword ());
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void updateUserWhenFirstNameIsWhitespace () throws Exception {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("         ");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (user.getFirstName ());
		updateUserBody.setLastName (user.getLastName ());
		updateUserBody.setEmail (user.getEmail ());
		updateUserBody.setUsername (user.getUsername ());
		updateUserBody.setPassword (user.getPassword ());
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void updateUserWhenLastNameIsWhitespace () throws Exception {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("            ");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (user.getFirstName ());
		updateUserBody.setLastName (user.getLastName ());
		updateUserBody.setEmail (user.getEmail ());
		updateUserBody.setUsername (user.getUsername ());
		updateUserBody.setPassword (user.getPassword ());
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void updateUserWhenEmailIsWhitespace () throws Exception {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("            ");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (user.getFirstName ());
		updateUserBody.setLastName (user.getLastName ());
		updateUserBody.setEmail (user.getEmail ());
		updateUserBody.setUsername (user.getUsername ());
		updateUserBody.setPassword (user.getPassword ());
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void updateUserWhenUsernameIsWhitespace () throws Exception {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("        ");
		user.setPassword ("password");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (user.getFirstName ());
		updateUserBody.setLastName (user.getLastName ());
		updateUserBody.setEmail (user.getEmail ());
		updateUserBody.setUsername (user.getUsername ());
		updateUserBody.setPassword (user.getPassword ());
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void updateUserWhenPasswordIsWhitespace () throws Exception {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("       ");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (user.getFirstName ());
		updateUserBody.setLastName (user.getLastName ());
		updateUserBody.setEmail (user.getEmail ());
		updateUserBody.setUsername (user.getUsername ());
		updateUserBody.setPassword (user.getPassword ());
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid user")))));
	}
	
	@Test
	void updateUserWhenUsernameIsTaken () throws Exception {
		User user1 = new User ();
		
		user1.setId (1);
		user1.setFirstName ("John");
		user1.setLastName ("Smith");
		user1.setEmail ("johnsmith@example.com");
		user1.setUsername ("johnsmith");
		user1.setPassword ("password");
		
		User user2 = new User ();
		
		user2.setId (2);
		user2.setFirstName ("John");
		user2.setLastName ("Smith");
		user2.setEmail ("johnsmith@example.com");
		user2.setUsername ("johnsmith");
		user2.setPassword ("password");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user1);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (user1.getFirstName ());
		updateUserBody.setLastName (user1.getLastName ());
		updateUserBody.setEmail (user1.getEmail ());
		updateUserBody.setUsername (user1.getUsername ());
		updateUserBody.setPassword (user1.getPassword ());
		
		Mockito.when (userService.getUserByUsername (user1.getUsername ())).thenReturn (user2);
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user1.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Username already in use")))));
	}
	
	@Test
	void updateUserWhenEmailIsTaken () throws Exception {
		User user1 = new User ();
		
		user1.setId (1);
		user1.setFirstName ("John");
		user1.setLastName ("Smith");
		user1.setEmail ("johnsmith@example.com");
		user1.setUsername ("johnsmith");
		user1.setPassword ("password");
		
		User user2 = new User ();
		
		user2.setId (2);
		user2.setFirstName ("John");
		user2.setLastName ("Smith");
		user2.setEmail ("johnsmith@example.com");
		user2.setUsername ("johnsmith");
		user2.setPassword ("password");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user1);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (user1.getFirstName ());
		updateUserBody.setLastName (user1.getLastName ());
		updateUserBody.setEmail (user1.getEmail ());
		updateUserBody.setUsername (user1.getUsername ());
		updateUserBody.setPassword (user1.getPassword ());
		
		Mockito.when (userService.getUserByEmail (user1.getEmail ())).thenReturn (user2);
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user1.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Email already in use")))));
	}
	
	@Test
	void updateUserWhenUsernameIsInvalid () throws Exception {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("@/.");
		user.setPassword ("password");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (user.getFirstName ());
		updateUserBody.setLastName (user.getLastName ());
		updateUserBody.setEmail (user.getEmail ());
		updateUserBody.setUsername (user.getUsername ());
		updateUserBody.setPassword (user.getPassword ());
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid username")))));
	}
	
	@Test
	void updateUserWhenEmailIsInvalid () throws Exception {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("not a valid email");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (user.getFirstName ());
		updateUserBody.setLastName (user.getLastName ());
		updateUserBody.setEmail (user.getEmail ());
		updateUserBody.setUsername (user.getUsername ());
		updateUserBody.setPassword (user.getPassword ());
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isBadRequest ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse (new InvalidValueException ("Invalid email")))));
	}
	
	@Test
	void updateUser () throws Exception {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (user.getFirstName ());
		updateUserBody.setLastName (user.getLastName ());
		updateUserBody.setEmail (user.getEmail ());
		updateUserBody.setUsername (user.getUsername ());
		updateUserBody.setPassword (user.getPassword ());
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isOk ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse ("Updated user", true, user))));
	}
	
	@Test
	void updateUserWithImage () throws Exception {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", user);
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (user.getFirstName ());
		updateUserBody.setLastName (user.getLastName ());
		updateUserBody.setEmail (user.getEmail ());
		updateUserBody.setUsername (user.getUsername ());
		updateUserBody.setPassword (user.getPassword ());
		
		Map <String, String> imageData = new HashMap <> ();
		
		imageData.put ("fileName", "test.png");
		imageData.put ("data", "testdata");
		
		updateUserBody.setProfileImageData (imageData);
		
		mvc.perform (MockMvcRequestBuilders.put ("/user/" + user.getId ())
			.contentType (MediaType.APPLICATION_JSON)
			.content (json.writeValueAsString (new UpdateUserBody ()))
			.session (mockHttpSession))
			
			.andExpect (MockMvcResultMatchers.status ().isOk ())
			.andExpect (MockMvcResultMatchers.content ().json (json.writeValueAsString (new JsonResponse ("Updated user", true, user))));
	}
}
