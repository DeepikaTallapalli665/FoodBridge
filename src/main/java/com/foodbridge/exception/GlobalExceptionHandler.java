package com.foodbridge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.foodbridge.user.exception.EmailAlreadyExistsException;
import com.foodbridge.user.exception.InvalidCredentialsException;
import com.foodbridge.user.exception.InvalidRoleException;
import com.foodbridge.user.exception.PhoneNumberAlreadyExistsException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<String> handleEmailException(EmailAlreadyExistsException ex) {
	    return ResponseEntity.badRequest().body(ex.getMessage());
	}
	@ExceptionHandler(PhoneNumberAlreadyExistsException.class)
	public ResponseEntity<String> handlePhoneException(PhoneNumberAlreadyExistsException ex) {
	    return ResponseEntity.badRequest().body(ex.getMessage());
	}
	@ExceptionHandler(InvalidRoleException.class)
	public ResponseEntity<String> handleRoleException(InvalidRoleException ex) {
	    return ResponseEntity.badRequest().body(ex.getMessage());
	}
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<String> handleInvalidCredentials(
	        InvalidCredentialsException ex) {

	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	            .body(ex.getMessage());
	}
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFound(
	        ResourceNotFoundException ex) {

	    return ResponseEntity
	            .status(HttpStatus.NOT_FOUND)
	            .body(ex.getMessage());
	}
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<String> handleBadRequest(
	        BadRequestException ex) {

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(ex.getMessage());
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationException(
	        MethodArgumentNotValidException ex) {

	    String message = ex.getBindingResult()
	            .getFieldErrors()
	            .get(0)
	            .getDefaultMessage();

	    return ResponseEntity
	            .badRequest()
	            .body(message);
	}

}