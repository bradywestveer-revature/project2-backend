package com.revature.project2backend.controllers;

import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.exceptions.UnauthorizedException;
import com.revature.project2backend.jsonmodels.JsonResponse;
import com.revature.project2backend.models.User;
import com.revature.project2backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping ("user")
@CrossOrigin (origins = "http://localhost:4200", allowCredentials = "true")
public class UserController {
	private final UserService userService;
	
	@Autowired
	public UserController (UserService userService) {
		this.userService = userService;
	}
	
	private void validateUser (User user) throws InvalidValueException {
		if (user.getFirstName () == null || user.getLastName () == null || user.getEmail () == null || user.getUsername () == null || user.getPassword () == null) {
			throw new InvalidValueException ("Error! Invalid user");
		}
		
		if (user.getFirstName ().trim ().equals ("") || user.getLastName ().trim ().equals ("") || user.getEmail ().trim ().equals ("") || user.getUsername ().trim ().equals ("") || user.getPassword ().trim ().equals ("")) {
			throw new InvalidValueException ("Error! Invalid user");
		}
		
		//todo check for invalid format?
	}
	
	@PostMapping
	public ResponseEntity <JsonResponse> createUser (@RequestBody User user) throws InvalidValueException {
		validateUser (user);
		
		if (userService.getUserByUsername (user.getUsername ()) != null) {
			throw new InvalidValueException ("Error! Username already in use");
		}
		
		if (userService.getUserByEmail (user.getEmail ()) != null) {
			throw new InvalidValueException ("Error! Email already in use");
		}
		
		this.userService.createUser (user);
		
		return ResponseEntity.ok (new JsonResponse ("Created user", true, null, "login"));
	}
	
	@GetMapping
	public ResponseEntity <JsonResponse> getUsers (HttpSession httpSession) throws UnauthorizedException {
		if (httpSession.getAttribute ("user") == null) {
			throw new UnauthorizedException ();
		}
		
		List <User> users = userService.getUsers ();
		
		return ResponseEntity.ok (new JsonResponse ("Found " + users.size () + " users", true, users));
	}
	
	@GetMapping ("{id}")
	public ResponseEntity <JsonResponse> getUser (@PathVariable Integer id, HttpSession httpSession) throws UnauthorizedException {
		if (httpSession.getAttribute ("user") == null) {
			throw new UnauthorizedException ();
		}
		
		return ResponseEntity.ok (new JsonResponse ("Found user", true, userService.getUser (id)));
	}
	
	@PutMapping ("{id}")
	public ResponseEntity <JsonResponse> updateUser (@PathVariable Integer id, @RequestBody Map <String, Object> body, HttpSession httpSession) throws InvalidValueException, UnauthorizedException {
		User sessionUser = (User) httpSession.getAttribute ("user");
		
		if (sessionUser == null || !sessionUser.getId ().equals (id)) {
			throw new UnauthorizedException ();
		}
		
		User user = userService.getUser (id);
		
		user.setFirstName (body.getOrDefault ("firstName", user.getFirstName ()).toString ());
		user.setLastName (body.getOrDefault ("lastName", user.getLastName ()).toString ());
		user.setEmail (body.getOrDefault ("email", user.getEmail ()).toString ());
		user.setUsername (body.getOrDefault ("username", user.getUsername ()).toString ());
		user.setPassword (body.getOrDefault ("password", user.getPassword ()).toString ());
		user.setProfileImagePath (body.getOrDefault ("profileImagePath", user.getProfileImagePath ()).toString ());
		
		validateUser (user);
		
		if (userService.getUserByUsername (user.getUsername ()) != null) {
			throw new InvalidValueException ("Error! Username already in use");
		}
		
		if (userService.getUserByEmail (user.getEmail ()) != null) {
			throw new InvalidValueException ("Error! Email already in use");
		}
		
		this.userService.updateUser (user);
		
		return ResponseEntity.ok (new JsonResponse ("Updated user", true, user));
	}
}
