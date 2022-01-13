package com.revature.project2backend.exceptions;

public class InvalidValueException extends  Exception {
	/**
	 *
	 * @param message
	 */
	public InvalidValueException (String message) {
		super ("Error! " + message);
	}
}
