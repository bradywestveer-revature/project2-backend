package com.revature.project2backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String email;
	private String profileImagePath;
}
