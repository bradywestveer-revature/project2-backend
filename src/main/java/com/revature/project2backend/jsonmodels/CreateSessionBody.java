package com.revature.project2backend.jsonmodels;

import lombok.Data;

@Data
public class CreateSessionBody {
	private String identifier;
	private String password;
}
