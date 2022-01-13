package com.revature.project2backend.controllers;

import com.revature.project2backend.exceptions.InvalidCredentialsException;
import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.jsonmodels.CreateSessionBody;
import com.revature.project2backend.jsonmodels.JsonResponse;
import com.revature.project2backend.models.User;
import com.revature.project2backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * The SessionController is responsible for creating and deleting a users Session to ensure proper security when logging in
 * and interacting with other methods.
 */
@RestController
@RequestMapping ("session")
@CrossOrigin (origins = "${PROJECT2_FRONTEND_URL}", allowCredentials = "true")
public class SessionController {

	/**
	 * An instance of UserService used for accessing the methods in the class.
	 */
	private final UserService userService;

	/**
	 * This Constructor initializes UserService.
	 * @param userService An instance of UserService used for accessing the methods in the class.
	 */
	@Autowired
	public SessionController (UserService userService) {
		this.userService = userService;
	}

	/**
	 *
	 *
	 * @param body A DTO with an identifier (username or email) and a password
	 * @param httpSession A session started by the user
	 * @return A JsonResponse notifying the user that they have logged in
	 * @throws InvalidCredentialsException Thrown when user have the incorrect identifier or password
	 * @throws InvalidValueException Thrown when user have the incorrect identifier or password
	 */
	@PostMapping
	public ResponseEntity <JsonResponse> createSession (@RequestBody CreateSessionBody body, HttpSession httpSession) throws InvalidCredentialsException, InvalidValueException {
		if (body.getIdentifier () == null || body.getPassword () == null) {
			throw new InvalidValueException ("Invalid credentials");
		}
		
		User user = this.userService.loginUser (body.getIdentifier (), body.getPassword ());
		
		httpSession.setAttribute ("user", user);
		
		return ResponseEntity.ok (new JsonResponse ("Logged in", true, user, "/"));
	}

	/**
	 * Deletes a currently active session.
	 *
	 * @param httpSession A session started by the user
	 * @return A JsonResponse letting the user know they have logged out
	 */
	@DeleteMapping
	public ResponseEntity <JsonResponse> deleteSession (HttpSession httpSession) {
		httpSession.invalidate ();
		
		return ResponseEntity.ok (new JsonResponse ("Logged out", true, null, "/login"));
	}
}
