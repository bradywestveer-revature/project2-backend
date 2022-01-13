package com.revature.project2backend.exceptions;

/**
 * UnauthorizedException is thrown when a user is not permitted to perform an action
 */
public class UnauthorizedException extends Exception {
	/**
	 * This constructor sets the message of the exception
	 */
	public UnauthorizedException () {
		super ("Error! Unauthorized");
	}
}
