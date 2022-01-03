package com.revature.project2backend;

import com.revature.project2backend.exceptions.InvalidCredentialsException;
import com.revature.project2backend.exceptions.InvalidValueException;
import com.revature.project2backend.exceptions.NotFoundException;
import com.revature.project2backend.exceptions.UnauthorizedException;
import com.revature.project2backend.jsonmodels.JsonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler
	public ResponseEntity <JsonResponse> invalidValueExceptionHandler (InvalidValueException exception) {
		return ResponseEntity.status (HttpStatus.BAD_REQUEST).body (new JsonResponse (exception));
	}
	
	@ExceptionHandler
	public ResponseEntity <JsonResponse> unauthorizedExceptionHandler (UnauthorizedException exception) {
		return ResponseEntity.status (HttpStatus.UNAUTHORIZED).body (new JsonResponse (exception, "/login"));
	}
	
	@ExceptionHandler
	public ResponseEntity <JsonResponse> invalidCredentialsExceptionHandler (InvalidCredentialsException exception) {
		return ResponseEntity.status (HttpStatus.UNAUTHORIZED).body (new JsonResponse (exception));
	}
	
	@ExceptionHandler
	public ResponseEntity <JsonResponse> notFoundExceptionHandler (NotFoundException exception) {
		return ResponseEntity.status (HttpStatus.NOT_FOUND).body (new JsonResponse (exception));
	}
	
	@ExceptionHandler
	public ResponseEntity <JsonResponse> exceptionHandler (Exception exception) {
		return ResponseEntity.status (HttpStatus.INTERNAL_SERVER_ERROR).body (new JsonResponse (exception));
	}
}
