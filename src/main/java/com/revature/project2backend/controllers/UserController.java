package com.revature.project2backend.controllers;

import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.exceptions.NotFoundException;
import com.revature.project2backend.exceptions.UnauthorizedException;
import com.revature.project2backend.jsonmodels.JsonResponse;
import com.revature.project2backend.jsonmodels.UpdateUserBody;
import com.revature.project2backend.models.User;
import com.revature.project2backend.services.UserService;
import com.revature.project2backend.utilities.S3Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * The UserController is responsible for mapping all endpoints necessary for passing data, pertaining to Users, from
 * the client to the server.
 */
@RestController
@RequestMapping ("user")
@CrossOrigin (origins = "${PROJECT2_FRONTEND_URL}", allowCredentials = "true")
public class UserController {

	/**
	 * An instance of UserService used for accessing the methods in the class.
	 */
	private final UserService userService;

	/**
	 * This Constructor initializes UserService.
	 *
	 * @param userService An instance of UserService used for accessing the methods in the class.
	 */
	@Autowired
	public UserController (UserService userService) {
		this.userService = userService;
	}

	/**
	 * Validates a user upon creation or when updating.
	 *
	 * @param user A User Object that is either new or being updated
	 * @throws InvalidValueException Thrown when the user is not valid or the username is already in use.
	 */
	private void validateUser (User user) throws InvalidValueException {
		if (user.getFirstName () == null || user.getLastName () == null || user.getEmail () == null || user.getUsername () == null || user.getPassword () == null) {
			throw new InvalidValueException ("Invalid user");
		}
		
		if (user.getFirstName ().trim ().equals ("") || user.getLastName ().trim ().equals ("") || user.getEmail ().trim ().equals ("") || user.getUsername ().trim ().equals ("") || user.getPassword ().trim ().equals ("")) {
			throw new InvalidValueException ("Invalid user");
		}
		
		User userWithUsername = userService.getUserByUsername (user.getUsername ());
		
		//if user with username exists and is not the same user as user
		if (userWithUsername != null && !userWithUsername.getId ().equals (user.getId ())) {
			throw new InvalidValueException ("Username already in use");
		}
		
		User userWithEmail = userService.getUserByEmail (user.getEmail ());
		
		//if user with email exists and is not the same user as user
		if (userWithEmail != null && !userWithEmail.getId ().equals (user.getId ())) {
			throw new InvalidValueException ("Email already in use");
		}
		
		if (!user.getUsername ().matches ("^[\\w-]+$")) {
			throw new InvalidValueException ("Invalid username");
		}
		
		if (!user.getEmail ().matches ("^[\\w-\\.]+@[\\w-]+\\.[a-zA-z]+$")) {
			throw new InvalidValueException ("Invalid email");
		}
	}

	/**
	 * Creates a user and send a JsonResponse back stating that the creation was a success.
	 *
	 * @param body A User Object that is being saved to the database
	 * @return a JsonResponse stating that the creation was a success.
	 * @throws InvalidValueException Thrown when a username or email is already in use.
	 */
	@PostMapping
	public ResponseEntity <JsonResponse> createUser (@RequestBody User body) throws InvalidValueException {
		validateUser (body);
		
		this.userService.createUser (body);
		
		return ResponseEntity.ok (new JsonResponse ("Created user", true, null, "/login"));
	}

	/**
	 * Gets all Users on the current page
	 *
	 * @param httpSession A session started by the user
	 * @return A List User Object belonging to the current page
	 * @throws UnauthorizedException Thrown when the user is not associated with the session
	 */
	@GetMapping
	public ResponseEntity <JsonResponse> getUsers (HttpSession httpSession) throws UnauthorizedException {
		if (httpSession.getAttribute ("user") == null) {
			throw new UnauthorizedException ();
		}
		
		List <User> users = userService.getUsers ();
		
		return ResponseEntity.ok (new JsonResponse ("Found " + users.size () + " users", true, users));
	}

	/**
	 * Uses a path parameter to find one specific User.
	 *
	 * @param id A unique Integer associated with User
	 * @param httpSession A session started by the user
	 * @return A user with the specified id
	 * @throws UnauthorizedException Thrown when the user is not associated with the session
	 * @throws NotFoundException Thrown when the user is not found
	 */
	@GetMapping ("{id}")
	public ResponseEntity <JsonResponse> getUser (@PathVariable Integer id, HttpSession httpSession) throws UnauthorizedException, NotFoundException {
		if (httpSession.getAttribute ("user") == null) {
			throw new UnauthorizedException ();
		}
		
		return ResponseEntity.ok (new JsonResponse ("Found user", true, userService.getUser (id)));
	}

	/**
	 * Uses a path parameter to update one specific User.
	 *
	 * @param id A unique Integer associated with User
	 * @param body A DTO containing all the fields in User Object that can be updated
	 * @param httpSession A session started by the user
	 * @return A JsonResponse notifying the user that the update was successful
	 * @throws InvalidValueException Thrown if the username or email in the body is already taken
	 * @throws UnauthorizedException Thrown when the user is not associated with the session
	 * @throws NotFoundException Thrown when the user is not found
	 */
	@PutMapping ("{id}")
	public ResponseEntity <JsonResponse> updateUser (@PathVariable Integer id, @RequestBody UpdateUserBody body, HttpSession httpSession) throws InvalidValueException, UnauthorizedException, NotFoundException {
		User user = (User) httpSession.getAttribute ("user");
		
		if (user == null) {
			throw new UnauthorizedException ();
		}
		
		if (!user.getId ().equals (id)) {
			throw new UnauthorizedException ();
		}
		
		if (body.getFirstName () != null) {
			user.setFirstName (body.getFirstName ());
		}
		
		if (body.getLastName () != null) {
			user.setLastName (body.getLastName ());
		}
		
		if (body.getEmail () != null) {
			user.setEmail (body.getEmail ());
		}
		
		if (body.getUsername () != null) {
			user.setUsername (body.getUsername ());
		}
		
		if (body.getPassword () != null) {
			user.setPassword (body.getPassword ());
		}
		
		if (body.getProfileImageData () != null) {
			String imageFileName = body.getProfileImageData ().getOrDefault ("fileName", null);
			String imageData = body.getProfileImageData ().getOrDefault ("data", null);
			
			if (imageFileName != null && imageData != null) {
				String path = System.currentTimeMillis () + user.getUsername () + imageFileName;
				
				//todo move image upload stuff to UserService?
				S3Utilities.uploadImage (path, imageData);
				
				user.setProfileImageUrl (S3Utilities.url + path);
			}
		}
		
		validateUser (user);
		
		this.userService.updateUser (user);
		
		return ResponseEntity.ok (new JsonResponse ("Updated user", true, user));
	}
}
