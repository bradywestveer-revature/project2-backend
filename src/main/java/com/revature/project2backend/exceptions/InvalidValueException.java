package com.revature.project2backend.exceptions;

public class InvalidValueException extends  Exception {
	public InvalidValueException (String message) {
		super ("Error! " + message);
	}
}
