package com.revature.project2backend.controllers;

import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.exceptions.NotFoundException;
import com.revature.project2backend.exceptions.UnauthorizedException;
import com.revature.project2backend.jsonmodels.JsonResponse;
import com.revature.project2backend.jsonmodels.UpdateUserBody;
import com.revature.project2backend.models.User;
import com.revature.project2backend.services.UserService;
import com.revature.project2backend.utilities.S3Utilities;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
	private final UserController userController;
	private final UserService userService = Mockito.mock (UserService.class);
	
	private final List <User> users = new ArrayList <> ();
	
	public UserControllerTest () {
		this.userController = new UserController (this.userService);
		
		users.add (new User (1, "John", "Smith", "johnsmith@example.com", "johnsmith", "password"));
		users.add (new User (2, "Sarah", "Smith", "sarahsmith@example.com", "sarahsmith", "password"));
	}
	
	@Test
	void createUserWhenFirstNameIsNull () {
		User user = new User ();
		
		user.setId (1);
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.createUser (user));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).createUser (Mockito.any ());
	}
	
	@Test
	void createUserWhenLastNameIsNull () {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.createUser (user));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).createUser (Mockito.any ());
	}
	
	@Test
	void createUserWhenEmailIsNull () {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.createUser (user));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).createUser (Mockito.any ());
	}
	
	@Test
	void createUserWhenUsernameIsNull () {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setPassword ("password");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.createUser (user));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).createUser (Mockito.any ());
	}
	
	@Test
	void createUserWhenPasswordIsNull () {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.createUser (user));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).createUser (Mockito.any ());
	}
	
	@Test
	void createUserWhenFirstNameIsEmpty () {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.createUser (user));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).createUser (Mockito.any ());
	}
	
	@Test
	void createUserWhenLastNameIsEmpty () {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.createUser (user));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).createUser (Mockito.any ());
	}
	
	@Test
	void createUserWhenEmailIsEmpty () {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.createUser (user));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).createUser (Mockito.any ());
	}
	
	@Test
	void createUserWhenUsernameIsEmpty () {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("");
		user.setPassword ("password");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.createUser (user));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).createUser (Mockito.any ());
	}
	
	@Test
	void createUserWhenPasswordIsEmpty () {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.createUser (user));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).createUser (Mockito.any ());
	}
	
	@Test
	void createUserWhenFirstNameIsWhitespace () {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("       ");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.createUser (user));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).createUser (Mockito.any ());
	}
	
	@Test
	void createUserWhenLastNameIsWhitespace () {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("          ");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.createUser (user));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).createUser (Mockito.any ());
	}
	
	@Test
	void createUserWhenEmailIsWhitespace () {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("            ");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.createUser (user));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).createUser (Mockito.any ());
	}
	
	@Test
	void createUserWhenUsernameIsWhitespace () {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("          ");
		user.setPassword ("password");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.createUser (user));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).createUser (Mockito.any ());
	}
	
	@Test
	void createUserWhenPasswordIsWhitespace () {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("johnsmith");
		user.setPassword ("            ");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.createUser (user));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).createUser (Mockito.any ());
	}
	
	@Test
	void createUserWhenUsernameIsTaken () {
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
		
		Mockito.when (userService.getUserByUsername (user1.getUsername ())).thenReturn (user2);
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.createUser (user1));
		
		assertEquals ("Error! Username already in use", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).createUser (Mockito.any ());
	}
	
	@Test
	void createUserWhenEmailIsTaken () {
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
		
		Mockito.when (userService.getUserByEmail (user1.getEmail ())).thenReturn (user2);
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.createUser (user1));
		
		assertEquals ("Error! Email already in use", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).createUser (Mockito.any ());
	}
	
	@Test
	void createUserWhenUsernameIsInvalid () {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("johnsmith@example.com");
		user.setUsername ("@.\\/");
		user.setPassword ("password");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.createUser (user));
		
		assertEquals ("Error! Invalid username", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).createUser (Mockito.any ());
	}
	
	@Test
	void createUserWhenEmailIsInvalid () {
		User user = new User ();
		
		user.setId (1);
		user.setFirstName ("John");
		user.setLastName ("Smith");
		user.setEmail ("not an email");
		user.setUsername ("johnsmith");
		user.setPassword ("password");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.createUser (user));
		
		assertEquals ("Error! Invalid email", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).createUser (Mockito.any ());
	}
	
	@Test
	void createUser () throws InvalidValueException {
		assertEquals (ResponseEntity.ok (new JsonResponse ("Created user", true, null, "/login")), this.userController.createUser (this.users.get (0)));
		
		Mockito.verify (userService).createUser (users.get (0));
	}
	
	@Test
	void getUsersWhenNotLoggedIn () {
		assertThrows (UnauthorizedException.class, () -> this.userController.getUsers (new MockHttpSession ()));
	}
	
	@Test
	void getUsers () throws UnauthorizedException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		Mockito.when (userService.getUsers ()).thenReturn (users);
		
		assertEquals (ResponseEntity.ok (new JsonResponse ("Found " + users.size () + " users", true, users)), this.userController.getUsers (mockHttpSession));
	}
	
	@Test
	void getUserWhenNotLoggedIn () {
		assertThrows (UnauthorizedException.class, () -> this.userController.getUser (1, new MockHttpSession ()));
	}
	
	@Test
	void getUser () throws NotFoundException, UnauthorizedException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", new User ());
		
		Mockito.when (userService.getUser (1)).thenReturn (users.get (0));
		
		assertEquals (ResponseEntity.ok (new JsonResponse ("Found user", true, users.get (0))), this.userController.getUser (1, mockHttpSession));
	}
	
	@Test
	void updateUserWhenNotLoggedIn () throws NotFoundException {
		assertThrows (UnauthorizedException.class, () -> this.userController.updateUser (users.get (0).getId () + 1, new UpdateUserBody (), new MockHttpSession ()));
		
		Mockito.verify (userService, Mockito.never ()).updateUser (Mockito.any ());
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWhenIdDoesNotMatchUserId () throws NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		assertThrows (UnauthorizedException.class, () -> this.userController.updateUser (users.get (0).getId () + 1, updateUserBody, mockHttpSession));
		
		Mockito.verify (userService, Mockito.never ()).updateUser (Mockito.any ());
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWhenFirstNameIsNull () throws NotFoundException, InvalidValueException, UnauthorizedException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setLastName (users.get (1).getLastName ());
		updateUserBody.setEmail (users.get (1).getEmail ());
		updateUserBody.setUsername (users.get (1).getUsername ());
		updateUserBody.setPassword (users.get (1).getPassword ());
		
		assertEquals (ResponseEntity.ok (new JsonResponse ("Updated user", true, users.get (0))), this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		assertNotNull (users.get (0).getFirstName ());
		
		Mockito.verify (userService).updateUser (users.get (0));
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWhenLastNameIsNull () throws InvalidValueException, UnauthorizedException, NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (users.get (1).getFirstName ());
		updateUserBody.setEmail (users.get (1).getEmail ());
		updateUserBody.setUsername (users.get (1).getUsername ());
		updateUserBody.setPassword (users.get (1).getPassword ());
		
		assertEquals (ResponseEntity.ok (new JsonResponse ("Updated user", true, users.get (0))), this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		assertNotNull (users.get (0).getLastName ());
		
		Mockito.verify (userService).updateUser (users.get (0));
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWhenEmailIsNull () throws InvalidValueException, UnauthorizedException, NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (users.get (1).getFirstName ());
		updateUserBody.setLastName (users.get (1).getLastName ());
		updateUserBody.setUsername (users.get (1).getUsername ());
		updateUserBody.setPassword (users.get (1).getPassword ());
		
		assertEquals (ResponseEntity.ok (new JsonResponse ("Updated user", true, users.get (0))), this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		assertNotNull (users.get (0).getEmail ());
		
		Mockito.verify (userService).updateUser (users.get (0));
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWhenUsernameIsNull () throws InvalidValueException, UnauthorizedException, NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (users.get (1).getFirstName ());
		updateUserBody.setLastName (users.get (1).getLastName ());
		updateUserBody.setEmail (users.get (1).getEmail ());
		updateUserBody.setPassword (users.get (1).getPassword ());
		
		assertEquals (ResponseEntity.ok (new JsonResponse ("Updated user", true, users.get (0))), this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		assertNotNull (users.get (0).getUsername ());
		
		Mockito.verify (userService).updateUser (users.get (0));
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWhenPasswordIsNull () throws NotFoundException, InvalidValueException, UnauthorizedException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (users.get (1).getFirstName ());
		updateUserBody.setLastName (users.get (1).getLastName ());
		updateUserBody.setEmail (users.get (1).getEmail ());
		updateUserBody.setUsername (users.get (1).getUsername ());
		
		assertEquals (ResponseEntity.ok (new JsonResponse ("Updated user", true, users.get (0))), this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		assertNotNull (users.get (0).getPassword ());
		
		Mockito.verify (userService).updateUser (users.get (0));
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWhenFirstNameIsEmpty () throws NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName ("");
		updateUserBody.setLastName (users.get (1).getLastName ());
		updateUserBody.setEmail (users.get (1).getEmail ());
		updateUserBody.setUsername (users.get (1).getUsername ());
		updateUserBody.setPassword (users.get (1).getPassword ());
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).updateUser (Mockito.any ());
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWhenLastNameIsEmpty () throws NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (users.get (1).getFirstName ());
		updateUserBody.setLastName ("");
		updateUserBody.setEmail (users.get (1).getEmail ());
		updateUserBody.setUsername (users.get (1).getUsername ());
		updateUserBody.setPassword (users.get (1).getPassword ());
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).updateUser (Mockito.any ());
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWhenEmailIsEmpty () throws NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (users.get (1).getFirstName ());
		updateUserBody.setLastName (users.get (1).getLastName ());
		updateUserBody.setEmail ("");
		updateUserBody.setUsername (users.get (1).getUsername ());
		updateUserBody.setPassword (users.get (1).getPassword ());
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).updateUser (Mockito.any ());
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWhenUsernameIsEmpty () throws NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (users.get (1).getFirstName ());
		updateUserBody.setLastName (users.get (1).getLastName ());
		updateUserBody.setEmail (users.get (1).getEmail ());
		updateUserBody.setUsername ("");
		updateUserBody.setPassword (users.get (1).getPassword ());
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).updateUser (Mockito.any ());
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWhenPasswordIsEmpty () throws NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (users.get (1).getFirstName ());
		updateUserBody.setLastName (users.get (1).getLastName ());
		updateUserBody.setEmail (users.get (1).getEmail ());
		updateUserBody.setUsername (users.get (1).getUsername ());
		updateUserBody.setPassword ("");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).updateUser (Mockito.any ());
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWhenFirstNameIsWhitespace () throws NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName ("      ");
		updateUserBody.setLastName (users.get (1).getLastName ());
		updateUserBody.setEmail (users.get (1).getEmail ());
		updateUserBody.setUsername (users.get (1).getUsername ());
		updateUserBody.setPassword (users.get (1).getPassword ());
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).updateUser (Mockito.any ());
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWhenLastNameIsWhitespace () throws NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (users.get (1).getFirstName ());
		updateUserBody.setLastName ("           ");
		updateUserBody.setEmail (users.get (1).getEmail ());
		updateUserBody.setUsername (users.get (1).getUsername ());
		updateUserBody.setPassword (users.get (1).getPassword ());
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).updateUser (Mockito.any ());
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWhenEmailIsWhitespace () throws NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (users.get (1).getFirstName ());
		updateUserBody.setLastName (users.get (1).getLastName ());
		updateUserBody.setEmail ("     ");
		updateUserBody.setUsername (users.get (1).getUsername ());
		updateUserBody.setPassword (users.get (1).getPassword ());
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).updateUser (Mockito.any ());
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWhenUsernameIsWhitespace () throws NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (users.get (1).getFirstName ());
		updateUserBody.setLastName (users.get (1).getLastName ());
		updateUserBody.setEmail (users.get (1).getEmail ());
		updateUserBody.setUsername ("       ");
		updateUserBody.setPassword (users.get (1).getPassword ());
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).updateUser (Mockito.any ());
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWhenPasswordIsWhitespace () throws NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (users.get (1).getFirstName ());
		updateUserBody.setLastName (users.get (1).getLastName ());
		updateUserBody.setEmail (users.get (1).getEmail ());
		updateUserBody.setUsername (users.get (1).getUsername ());
		updateUserBody.setPassword ("                 ");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		assertEquals ("Error! Invalid user", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).updateUser (Mockito.any ());
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWhenUsernameIsTaken () throws NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		Mockito.when (userService.getUserByUsername (users.get (1).getUsername ())).thenReturn (users.get (1));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setUsername (users.get (1).getUsername ());
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		assertEquals ("Error! Username already in use", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).updateUser (Mockito.any ());
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWhenEmailIsTaken () throws NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		Mockito.when (userService.getUserByEmail (users.get (1).getEmail ())).thenReturn (users.get (1));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setEmail (users.get (1).getEmail ());
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		assertEquals ("Error! Email already in use", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).updateUser (Mockito.any ());
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWhenUsernameIsInvalid () throws NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setUsername (".@/");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		assertEquals ("Error! Invalid username", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).updateUser (Mockito.any ());
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWhenEmailIsInvalid () throws NotFoundException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setEmail ("not an email");
		
		InvalidValueException exception = assertThrows (InvalidValueException.class, () -> this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		assertEquals ("Error! Invalid email", exception.getMessage ());
		
		Mockito.verify (userService, Mockito.never ()).updateUser (Mockito.any ());
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUser () throws NotFoundException, InvalidValueException, UnauthorizedException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (users.get (1).getFirstName ());
		updateUserBody.setLastName (users.get (1).getLastName ());
		updateUserBody.setEmail (users.get (1).getEmail ());
		updateUserBody.setUsername (users.get (1).getUsername ());
		updateUserBody.setPassword (users.get (1).getPassword ());
		
		assertEquals (ResponseEntity.ok (new JsonResponse ("Updated user", true, users.get (0))), this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		Mockito.verify (userService).updateUser (users.get (0));
		
		//todo make sure s3utilities.uploadImage is not called
		
		assertEquals (S3Utilities.url + "profile.jpg", users.get (0).getProfileImageUrl ());
	}
	
	@Test
	void updateUserWithImage () throws NotFoundException, InvalidValueException, UnauthorizedException {
		MockHttpSession mockHttpSession = new MockHttpSession ();
		
		mockHttpSession.setAttribute ("user", users.get (0));
		
		UpdateUserBody updateUserBody = new UpdateUserBody ();
		
		updateUserBody.setFirstName (users.get (1).getFirstName ());
		updateUserBody.setLastName (users.get (1).getLastName ());
		updateUserBody.setEmail (users.get (1).getEmail ());
		updateUserBody.setUsername (users.get (1).getUsername ());
		updateUserBody.setPassword (users.get (1).getPassword ());
		
		Map <String, String> imageData = new HashMap <> ();
		
		imageData.put ("fileName", "test.png");
		imageData.put ("data", "testdata");
		
		updateUserBody.setProfileImageData (imageData);
		
		assertEquals (ResponseEntity.ok (new JsonResponse ("Updated user", true, users.get (0))), this.userController.updateUser (users.get (0).getId (), updateUserBody, mockHttpSession));
		
		Mockito.verify (userService).updateUser (users.get (0));
		
		//todo mock s3utilities
		//todo make sure s3utilities.uploadImage was called
		
		assertTrue (users.get (0).getProfileImageUrl ().matches ("\\Q" + S3Utilities.url + "\\E\\d+\\Q" + users.get (0).getUsername () + imageData.get ("fileName") + "\\E"));
	}
}