package com.nt.Backend_NT.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.Backend_NT.entities.InventoryEntity;
import com.nt.Backend_NT.entities.LabelEntity;
import com.nt.Backend_NT.entities.LoanEntity;
import com.nt.Backend_NT.entities.ProductEntity;
import com.nt.Backend_NT.exceptions.BadRequestException;
import com.nt.Backend_NT.exceptions.ConflictException;
import com.nt.Backend_NT.model.LabelInventoryRequest;
import com.nt.Backend_NT.model.LoanRequest;
import com.nt.Backend_NT.repositories.InventoryRepository;
import com.nt.Backend_NT.repositories.LoanRepository;

@Service
public class LoanService {
	@Autowired
	private LoanRepository loanRepository;
	@Autowired
	private ProductService productService;
	@Autowired 
	private LoanStateService loanStateService;
	@Autowired 
	private LoanXLabelService loanXLabelService;
	@Autowired
	private LabelService labelService;
	@Autowired
	private InventoryRepository inventoryRepository;
	


	public LoanEntity createLoan(LoanRequest loan) throws Exception {
		
		ProductEntity productEntity = 
				productService.getProductByReference(loan.getProducto());
		
		if(loan.getFecha() != null && loan.getHora() != null && 
				isValidAmountToUpdate(loan.getEtiquetas())) {
			
			LoanEntity loanEntity = new LoanEntity();
			
			loanEntity.setProductReference(productEntity);
			loanEntity.setTitular(loan.getTitular());
			loanEntity.setAllocal(loan.getLocal());
			loanEntity.setCantidad(totalAmount(loan.getEtiquetas()));
			loanEntity.setEstado(loanStateService.getLoanByState("PENDIENTE"));
			loanEntity.setFecha(loan.getFecha());
			loanEntity.setHora(loan.getHora());
			loanEntity.setCantidad(totalAmount(loan.getEtiquetas()));
				
			loanXLabelService.createLoanXLabel(loanRepository.save(loanEntity),
					loan.getEtiquetas());
			
			return loanEntity;
		}
		
		throw new BadRequestException("Debe proporcionar una fecha y hora para el préstamo");
	}

	public int totalAmount(List<LabelInventoryRequest> labels) {
		return labels.stream().mapToInt(l -> l.getCantidad()).sum();
	}

	public boolean isValidAmountToUpdate(List<LabelInventoryRequest> labels) throws Exception {

		for (LabelInventoryRequest label : labels) {
			LabelEntity labelInDB = labelService.getLabel(label.getId());
			InventoryEntity inventory = inventoryRepository.findByLabelReference(labelInDB);

			if (inventory.getCantidad() < label.getCantidad()) {
				throw new ConflictException(String.format(
						"No tiene suficiente inventario de la etiqueta %s para el préstamo", labelInDB.getNombre()));
			}

		}

		return true;
	}

}
