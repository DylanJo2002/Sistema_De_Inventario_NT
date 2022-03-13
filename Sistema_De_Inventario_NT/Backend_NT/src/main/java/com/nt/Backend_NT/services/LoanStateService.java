package com.nt.Backend_NT.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.Backend_NT.entities.LoanStatesEntity;
import com.nt.Backend_NT.model.LoanStateResponse;
import com.nt.Backend_NT.model.LoanStatesResponse;
import com.nt.Backend_NT.repositories.LoanStatesRepository;

@Service
public class LoanStateService {
	
	@Autowired
	private LoanStatesRepository loanStatesRepository;
	
    public LoanStatesResponse getLoanStates() {
    	LoanStatesResponse response = new LoanStatesResponse();
		List<LoanStatesEntity> loanStatesEntity = loanStatesRepository.findAll();
		List<LoanStateResponse> loanStates = new ArrayList<LoanStateResponse>();
		
		loanStatesEntity.forEach(e -> {
			LoanStateResponse loanState = new LoanStateResponse();
			
			loanState.setId(e.getId());
			loanState.setEstado(e.getEstado());
			loanStates.add(loanState);
		});
		
		response.setEstados(loanStates);
		
		return response;
	}
}
