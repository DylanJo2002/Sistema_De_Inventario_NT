package com.nt.Backend_NT.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nt.Backend_NT.exceptions.ConflictException;
import com.nt.Backend_NT.model.ErrorResponse;

@RestControllerAdvice
public class AdviseController {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> generalException(Exception ex) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse(ex.getMessage())
				,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ErrorResponse> conflictExceptioMessage(Exception ex) {
		return new ResponseEntity<ErrorResponse>(new ErrorResponse(ex.getMessage())
				,HttpStatus.CONFLICT);
	}
	
}
