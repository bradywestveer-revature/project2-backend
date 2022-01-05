package com.revature.project2backend.jsonmodels;

import lombok.Data;

@Data
public class UpdateUserBody {
	private String firstName;
	private String lastName;
	private String email;
	private String username;
	private String password;
	private String profileImageUrl;
}
