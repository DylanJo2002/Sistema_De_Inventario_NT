package com.nt.Backend_NT.controllers;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.nt.Backend_NT.model.ErrorResponse;

@RestControllerAdvice
public class AdviseController {
	
	@ExceptionHandler(Exception.class)
	public ErrorResponse conflictExceptioMessage(Exception ex) {
		return new ErrorResponse(ex.getMessage());
	}
	
}
