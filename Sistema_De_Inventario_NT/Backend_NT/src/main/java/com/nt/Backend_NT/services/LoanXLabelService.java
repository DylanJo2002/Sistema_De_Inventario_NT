package com.nt.Backend_NT.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.Backend_NT.entities.InventoryEntity;
import com.nt.Backend_NT.entities.LabelEntity;
import com.nt.Backend_NT.entities.LoanEntity;
import com.nt.Backend_NT.entities.LoanXLabelEntity;
import com.nt.Backend_NT.model.LabelInventoryRequest;
import com.nt.Backend_NT.repositories.InventoryRepository;
import com.nt.Backend_NT.repositories.LoanXLabelRepository;

@Service
public class LoanXLabelService {
	@Autowired
	private LoanXLabelRepository loanXLabelRepository;
	@Autowired
	private LabelService labelService;
	@Autowired
	private InventoryRepository inventoryRepository;
	
	
	public List<LoanXLabelEntity> createLoanXLabel(LoanEntity loan,List<LabelInventoryRequest> labels)
			throws Exception{
		
		List<LoanXLabelEntity> loanXLabelEntities = new ArrayList<LoanXLabelEntity>();
		
		for(LabelInventoryRequest label :labels ) {
			LoanXLabelEntity loanXLabelEntity = new LoanXLabelEntity();
			LabelEntity labelInDB = labelService.getLabel(label.getId());
			InventoryEntity inventoryEntity = inventoryEntity = inventoryRepository.findByLabelReference(labelInDB);
			
			int updatedAmont = inventoryEntity.getCantidad()-label.getCantidad();
			
			loanXLabelEntity.setPrestamo(loan);
			loanXLabelEntity.setEtiqueta(labelInDB);
			loanXLabelEntity.setCantidad(label.getCantidad());
			loanXLabelEntities.add(loanXLabelRepository.save(loanXLabelEntity));
			
			inventoryEntity.setCantidad(updatedAmont);
			inventoryRepository.save(inventoryEntity);
		}
		
		
		return loanXLabelEntities;
	}

	public List<LoanXLabelEntity> updateLoanXLabel(LoanEntity loan,List<LabelInventoryRequest> labels)
			throws Exception{
		
		List<LoanXLabelEntity> loanXLabelEntities = new ArrayList<LoanXLabelEntity>();
		
		for(LabelInventoryRequest l : labels) {
			
			LabelEntity labelEntity = labelService.getLabel(l.getId());
			InventoryEntity inventoryEntity = inventoryRepository.findByLabelReference(labelEntity);
			LoanXLabelEntity loanXLabelEntity = loanXLabelRepository
					.findByPrestamoAndEtiqueta(loan, labelEntity);
			
			int updatedAmount = inventoryEntity.getCantidad()+loanXLabelEntity.getCantidad()
				-l.getCantidad();
			
			inventoryEntity.setCantidad(updatedAmount);
			loanXLabelEntity.setCantidad(l.getCantidad());
			
			inventoryRepository.save(inventoryEntity);
			loanXLabelRepository.save(loanXLabelEntity);
			
			loanXLabelEntities.add(loanXLabelEntity);
			
		};
		
		
		return loanXLabelEntities;
	}
	
	
	public LoanXLabelEntity getLoanXLabelEntityByLoan(LoanEntity loan, LabelEntity label){
		
		return loanXLabelRepository.findByPrestamoAndEtiqueta(loan,label);
	}
	
	public List<LoanXLabelEntity> getLoansXLabelByLoan(LoanEntity loan){
		return loanXLabelRepository.findByPrestamo(loan);
	}
}
