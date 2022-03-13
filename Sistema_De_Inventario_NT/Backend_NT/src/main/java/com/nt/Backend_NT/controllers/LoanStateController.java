package com.nt.Backend_NT.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.Backend_NT.model.LoanStatesResponse;
import com.nt.Backend_NT.services.LoanStateService;

@RestController
@RequestMapping("/loanStates")
public class LoanStateController {
	@Autowired
	private LoanStateService loanStateService;
	
	@GetMapping
	public ResponseEntity<LoanStatesResponse> getLoanStates(){
		LoanStatesResponse response = loanStateService.getLoanStates();
		
		return new ResponseEntity<LoanStatesResponse>(response, HttpStatus.OK);
	}
}
