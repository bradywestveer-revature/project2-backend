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

@RestController
@RequestMapping ("user")
public class UserController {
	private final UserService userService;
	
	@Autowired
	public UserController (UserService userService) {
		this.userService = userService;
	}
	
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
	
	@PostMapping
	public ResponseEntity <JsonResponse> createUser (@RequestBody User body) throws InvalidValueException {
		validateUser (body);
		
		this.userService.createUser (body);
		
		return ResponseEntity.ok (new JsonResponse ("Created user", true, null, "/login"));
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
	public ResponseEntity <JsonResponse> getUser (@PathVariable Integer id, HttpSession httpSession) throws UnauthorizedException, NotFoundException {
		if (httpSession.getAttribute ("user") == null) {
			throw new UnauthorizedException ();
		}
		
		return ResponseEntity.ok (new JsonResponse ("Found user", true, userService.getUser (id)));
	}
	
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
				
				S3Utilities.uploadImage (path, imageData);
				
				user.setProfileImageUrl (S3Utilities.url + path);
			}
		}
		
		validateUser (user);
		
		this.userService.updateUser (user);
		
		return ResponseEntity.ok (new JsonResponse ("Updated user", true, user));
	}
}
