package com.revature.project2backend.exceptions;

public class NotFoundException extends Exception {
	public NotFoundException (String message) {
		super ("Error! " + message);
	}
}
