package com.revature.project2backend.exceptions;

/**
 * InvalidValueException is thrown when an issue arises from a value given by the client
 */
public class InvalidValueException extends Exception {
	/**
	 * This constructor sets the message of the exception
	 * @param message The message to use
	 */
	public InvalidValueException (String message) {
		super ("Error! " + message);
	}
}
