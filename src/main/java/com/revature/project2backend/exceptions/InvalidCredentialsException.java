package com.revature.project2backend.exceptions;

public class InvalidCredentialsException extends Exception {
	public InvalidCredentialsException () {
		super ("Error! Invalid credentials");
	}
}
