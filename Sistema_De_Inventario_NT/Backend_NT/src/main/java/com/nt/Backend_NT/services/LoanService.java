package com.nt.Backend_NT.services;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tomcat.jni.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.Backend_NT.entities.InventoryEntity;
import com.nt.Backend_NT.entities.LabelEntity;
import com.nt.Backend_NT.entities.LoanEntity;
import com.nt.Backend_NT.entities.LoanStatesEntity;
import com.nt.Backend_NT.entities.LoanXLabelEntity;
import com.nt.Backend_NT.entities.ProductEntity;
import com.nt.Backend_NT.entities.SaleEntity;
import com.nt.Backend_NT.entities.SaleXLabelEntity;
import com.nt.Backend_NT.exceptions.BadRequestException;
import com.nt.Backend_NT.exceptions.ConflictException;
import com.nt.Backend_NT.exceptions.NotFoundException;
import com.nt.Backend_NT.model.LabelInventoryRequest;
import com.nt.Backend_NT.model.LoanRequest;
import com.nt.Backend_NT.model.LoanUpdateRequest;
import com.nt.Backend_NT.repositories.InventoryRepository;
import com.nt.Backend_NT.repositories.LoanRepository;
import com.nt.Backend_NT.repositories.SaleRepository;
import com.nt.Backend_NT.repositories.SaleXLabelRepository;

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
	@Autowired
	private SaleRepository saleRepository;
	@Autowired
	private SaleXLabelRepository saleXLabelRepository;


	public LoanEntity createLoan(LoanRequest loan) throws Exception {
		
		ProductEntity productEntity = 
				productService.getProductByReference(loan.getProducto());
		
		if(loan.getFecha() != null && loan.getHora() != null && 
				isValidAmountToCreate(loan.getEtiquetas())) {
			
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

	public LoanEntity updateLoan(int loanId,LoanUpdateRequest loan) 
			throws Exception {
		
		LoanEntity loanEntity = loanRepository.getById(loanId);
		
		if(loanEntity != null) {
			
			if(loanEntity.getEstado().getEstado().equals("PENDIENTE")) {
				if(loan.getFecha() != null && loan.getHora() != null 
						&& isValidAmountToUpdate(loanEntity, loan.getEtiquetas())) {
					
					loanEntity.setTitular(loan.getTitular());
					loanEntity.setAllocal(loan.getLocal());
					loanEntity.setFecha(loan.getFecha());
					loanEntity.setHora(loan.getHora());
					
					loanXLabelService.updateLoanXLabel(loanEntity, loan.getEtiquetas());
					
					return loanRepository.save(loanEntity);
					
				}
				
				throw new BadRequestException("Debe proporcionar una fecha y hora para el préstamo");
			}
			throw new BadRequestException(String.format("No se puede editar el estado del préstamo "
					.concat("%d porque su estado es %s"), loanId,loanEntity.getEstado().getEstado()));
		}
		
		throw new NotFoundException(String.format("No se encontró el préstamo con "
				.concat("el id %d"), loanId));
	}
	
	public LoanEntity updateLoanStatus(int loanId, int statusID) throws Exception {

		LoanEntity loanEntity = loanRepository.findById(loanId);
		
		if(loanEntity != null) {
			LoanStatesEntity currentStatus = loanEntity.getEstado();
			
			if(currentStatus.getEstado().equals("PENDIENTE")) {
				LoanStatesEntity desiredLoanStatus = loanStateService.getLoanById(statusID);
				loanEntity.setEstado(desiredLoanStatus);
				List<LoanXLabelEntity> loanLabels = loanXLabelService
						.getLoansXLabelByLoan(loanEntity);
				if(desiredLoanStatus.getEstado().equals("VENDIDO")) {
					
					SaleEntity sale = new SaleEntity();
					sale.setProductReference(loanEntity.getProductReference());
					sale.setCantidadtotal(loanEntity.getCantidad());
					sale.setFecha(new Date());
					sale.setHora(LocalTime.now());
					
					sale = saleRepository.save(sale);

					
					for(LoanXLabelEntity l : loanLabels) {
						
						SaleXLabelEntity label = new SaleXLabelEntity();
						label.setVenta(sale);
						label.setEtiqueta(l.getEtiqueta());
						label.setCantidad(l.getCantidad());
						saleXLabelRepository.save(label);
					}
					
					
				}else {
					
					loanLabels.forEach(l -> {
						
						InventoryEntity inventory = inventoryRepository.findByLabelReference(l.getEtiqueta());
						inventory.setCantidad(inventory.getCantidad()+l.getCantidad());
								
						inventoryRepository.save(inventory);
					});
					
					
				}
				
				return loanRepository.save(loanEntity);
			}
			
			throw new BadRequestException(String.format("No se puede editar el estado del préstamo "
					.concat("%d porque su estado es %s"), loanId,currentStatus.getEstado()));
		}
		
		
		throw new NotFoundException(String.format("No se encontró el préstamo con "
				.concat("el id %d"), loanId));
	}
	

 	public int totalAmount(List<LabelInventoryRequest> labels) {
		return labels.stream().mapToInt(l -> l.getCantidad()).sum();
	}

	public boolean isValidAmountToCreate(List<LabelInventoryRequest> labels) throws Exception {

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

	public boolean isValidAmountToUpdate(LoanEntity loan,List<LabelInventoryRequest> labels) 
			throws Exception {

		for (LabelInventoryRequest label : labels) {
			LabelEntity labelInDB = labelService.getLabel(label.getId());
			InventoryEntity inventory = inventoryRepository.findByLabelReference(labelInDB);
			LoanXLabelEntity loanxLabel = loanXLabelService.getLoanXLabelEntityByLoan(loan, labelInDB);
			
			if(loanxLabel != null) {
				
				int updatedAmount = inventory.getCantidad()+loanxLabel.getCantidad()-label.getCantidad();
				
				if (updatedAmount < 0) {
					throw new ConflictException(String.format(
							"No tiene suficiente inventario de la etiqueta %s para el préstamo", labelInDB.getNombre()));
				}
				
			}else {
				throw new BadRequestException(String.format("No existe la etiqueta %s en el préstamo %d"
						, labelInDB.getNombre(), loan.getId()));	
			}

		}

		return true;
	}
}
