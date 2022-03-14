package com.nt.Backend_NT.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nt.Backend_NT.entities.LoanEntity;
import com.nt.Backend_NT.model.LoanRequest;
import com.nt.Backend_NT.model.LoanUpdateRequest;
import com.nt.Backend_NT.model.LoansResponse;
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
	
	@PutMapping("/{loanId}")
	public ResponseEntity<LoanEntity> updateLoan(@PathVariable int loanId,
			@RequestBody LoanUpdateRequest loan) throws Exception {
		
		LoanEntity loanEntity = loanService.updateLoan(loanId,loan);
		
		return new ResponseEntity<LoanEntity>(loanEntity, HttpStatus.OK);
	}

	@PutMapping("/status/{loanId}")
	public ResponseEntity<LoanEntity> updateLoanState(@PathVariable int loanId, @RequestParam int state)
			throws Exception{
		LoanEntity loanEntity = loanService.updateLoanStatus(loanId, state);
		return new ResponseEntity<LoanEntity>(loanEntity, HttpStatus.OK); 
	}

	@GetMapping
	public ResponseEntity<LoansResponse> getByCategory(@RequestParam int categoryId,
			@RequestParam String dateStart, @RequestParam String dateEnd, @RequestParam int stateId)
					throws Exception{
		
		LoansResponse response = loanService.getLoansByCategory(categoryId, dateStart
				, dateEnd, stateId);
		
		return new ResponseEntity<LoansResponse>(response, HttpStatus.OK);
	}

	@GetMapping("/{reference}")
	public ResponseEntity<LoansResponse> getByProduct(@PathVariable String reference,
			@RequestParam String dateStart, @RequestParam String dateEnd, @RequestParam int stateId)
					throws Exception{
		
		LoansResponse response = loanService.getLoansByProduct(reference, dateStart
				, dateEnd, stateId);
		
		return new ResponseEntity<LoansResponse>(response, HttpStatus.OK);
	}
}
