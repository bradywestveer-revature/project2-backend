package com.revature.project2backend.controllers;

import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.exceptions.UnauthorizedException;
import com.revature.project2backend.jsonmodels.CreateSessionDTO;
import com.revature.project2backend.jsonmodels.JsonResponse;
import com.revature.project2backend.models.User;
import com.revature.project2backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping ("session")
@CrossOrigin (origins = "http://localhost:4200", allowCredentials = "true")
public class SessionController {
	private final UserService userService;

	@Autowired
	public SessionController(UserService userService){
		this.userService = userService;
	}
	
	@PostMapping
	public ResponseEntity <JsonResponse> createSession (HttpSession httpSession, @RequestBody CreateSessionDTO createSessionDTO) {

		System.out.println("identifier="+createSessionDTO.getIdentifier() +" password="+createSessionDTO.getPassword());
		// Validate credentials
		User sessionUser = this.userService.validateCredentials(createSessionDTO.getIdentifier(), createSessionDTO.getPassword());

		if (sessionUser == null) {
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(new JsonResponse("Invalid username/email or password.", false, null));
		}

		httpSession.setAttribute("user", sessionUser);
		return ResponseEntity.ok (new JsonResponse("Login successful.", true, sessionUser, "@"+sessionUser.getUsername()));
	}

	@DeleteMapping
	public ResponseEntity<JsonResponse> deleteSession(HttpSession httpSession){
		httpSession.invalidate();
		return ResponseEntity.ok( new JsonResponse("You have been successfully logged out.", true, null, "login"));
	}

}
