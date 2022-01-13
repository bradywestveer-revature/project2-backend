package com.revature.project2backend.exceptions;

/**
 * NotFoundException is thrown when an entity is not found in the database
 */
public class NotFoundException extends Exception {
	/**
	 * This constructor sets the message of the exception
	 * @param message The message to use
	 */
	public NotFoundException (String message) {
		super ("Error! " + message);
	}
}
