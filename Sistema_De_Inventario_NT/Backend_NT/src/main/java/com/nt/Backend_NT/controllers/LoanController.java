package com.nt.Backend_NT.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.Backend_NT.entities.LoanEntity;
import com.nt.Backend_NT.model.LoanRequest;
import com.nt.Backend_NT.services.LoanService;

@RestController
@RequestMapping("/loans")
public class LoanController {
	@Autowired
	private LoanService loanService;
	
	@PostMapping
	public ResponseEntity<LoanEntity> createLoan(@RequestBody LoanRequest loan) throws Exception {
		
		LoanEntity loanEntity = loanService.createLoan(loan);
		
		return new ResponseEntity<LoanEntity>(loanEntity, HttpStatus.CREATED);
	}
}
