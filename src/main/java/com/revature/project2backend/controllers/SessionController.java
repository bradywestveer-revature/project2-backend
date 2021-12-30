package com.revature.project2backend.controllers;

import com.revature.project2backend.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("session")
@CrossOrigin (origins = "http://localhost:4200", allowCredentials = "true")
public class SessionController {
	private UserService userService;
	
	@PostMapping
	public void createSession (@RequestParam String identifier, @RequestParam String password) {
		
	}
}
