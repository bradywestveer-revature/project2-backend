package com.revature.project2backend.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("like")
@CrossOrigin (origins = "http://localhost:4200", allowCredentials = "true")
public class LikeController {
}
