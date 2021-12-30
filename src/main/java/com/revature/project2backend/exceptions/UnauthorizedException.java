package com.revature.project2backend.exceptions;

public class UnauthorizedException extends Exception {
	public UnauthorizedException () {
		super ("Error! Unauthorized");
	}
}
