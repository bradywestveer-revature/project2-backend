package com.revature.project2backend.jsonmodels;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * JsonResponse is used to send responses in a consistent format to clients
 * A JsonResponse instance has a message string, success boolean, data object, and redirect string 
 */
@NoArgsConstructor
@Data
public class JsonResponse {
	/**
	 * The message of the response
	 */
	private String message;
	
	/**
	 * Whether the response was successful
	 */
	private boolean success;
	
	/**
	 * The data being passed to the client
	 */
	private Object data;
	
	/**
	 * The redirect path of the response
	 */
	private String redirect;
	
	/**
	 * The log4j logger that logs the contents of responses
	 */
	private static final Logger logger = Logger.getLogger (JsonResponse.class);
	
	/**
	 * This constructor allows easily creating a JsonResponse with just an exception. It sets the message to the message of the exception, and sets the success value to false
	 * @param exception The exception to set the message from
	 */
	public JsonResponse (Exception exception) {
		this (exception.getMessage (), false);
	}
	
	/**
	 * This constructor takes in an exception and a redirect. It sets the message to the message of the exception, sets success to false, and allows setting a redirect path
	 * @param exception The exception to set the message from
	 * @param redirect The path to redirect the client to
	 */
	public JsonResponse (Exception exception, String redirect) {
		this (exception.getMessage (), false, null, redirect);
	}
	
	/**
	 * This constructor allows easily creating a JsonResponse with just an exception. It sets the message to the message of the exception, and sets the success value to false
	 * @param message The message to use
	 * @param success The success value to use
	 */
	public JsonResponse (String message, boolean success) {
		this (message, success, null, null);
	}
	
	/**
	 * This constructor takes in a message, success value, and an object to use for the response's data
	 * @param message The message to use
	 * @param success The success value to use
	 * @param data The data object to use
	 */
	public JsonResponse (String message, boolean success, Object data) {
		this (message, success, data, null);
	}
	
	/**
	 * This constructor takes in all arguments for the JsonResponse. It is called by all the other constructors in the class. It sets each value of the JsonResponse, and logs the JsonResponse's contents
	 * @param message The message to use
	 * @param success The success value to use
	 * @param data The data object to use
	 * @param redirect The redirect path to use
	 */
	public JsonResponse (String message, boolean success, Object data, String redirect) {
		this.message = message;
		this.success = success;
		this.data = data;
		this.redirect = redirect;
		
		try {
			logger.log (success ? Level.INFO : Level.ERROR, "Response: " + new ObjectMapper ().writeValueAsString (this));
		} catch (JsonProcessingException exception) {
			logger.error ("Error!", exception);
		}
	}
}