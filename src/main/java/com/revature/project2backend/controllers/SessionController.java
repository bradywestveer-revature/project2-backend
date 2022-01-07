package com.revature.project2backend.controllers;

import com.revature.project2backend.exceptions.InvalidCredentialsException;
import com.revature.project2backend.jsonmodels.CreateSessionBody;
import com.revature.project2backend.jsonmodels.JsonResponse;
import com.revature.project2backend.models.User;
import com.revature.project2backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping ("session")
public class SessionController {
	private final UserService userService;
	
	@Autowired
	public SessionController (UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping
	public ResponseEntity <JsonResponse> createSession (@RequestBody CreateSessionBody body, HttpSession httpSession) throws InvalidCredentialsException {
		if (body.getIdentifier () == null || body.getPassword () == null) {
			throw new InvalidCredentialsException ();
		}
		
		User user = this.userService.loginUser (body.getIdentifier (), body.getPassword ());
		
		httpSession.setAttribute ("user", user);
		
		return ResponseEntity.ok (new JsonResponse ("Logged in", true, user, "/"));
	}
	
	@DeleteMapping
	public ResponseEntity <JsonResponse> deleteSession (HttpSession httpSession) {
		httpSession.invalidate ();
		
		return ResponseEntity.ok (new JsonResponse ("Logged out", true, null, "/login"));
	}
}
