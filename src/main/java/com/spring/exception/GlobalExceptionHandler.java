package com.spring.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UrlNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUrlNotFound(UrlNotFoundException exception){
		return null;
	}
}
