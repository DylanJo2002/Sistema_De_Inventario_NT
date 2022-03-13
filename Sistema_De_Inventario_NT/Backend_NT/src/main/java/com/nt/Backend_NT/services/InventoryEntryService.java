package com.nt.Backend_NT.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.Backend_NT.entities.InventoryEntity;
import com.nt.Backend_NT.entities.InventoryEntryEntity;
import com.nt.Backend_NT.entities.InventoryEntryXLabelEntity;
import com.nt.Backend_NT.entities.LabelEntity;
import com.nt.Backend_NT.entities.ProductEntity;
import com.nt.Backend_NT.exceptions.BadRequestException;
import com.nt.Backend_NT.exceptions.NotFoundException;
import com.nt.Backend_NT.model.InventoryEntryRequest;
import com.nt.Backend_NT.model.InventoryEntryUpdateRequest;
import com.nt.Backend_NT.model.LabelInventoryRequest;
import com.nt.Backend_NT.repositories.InventoryEntryRepository;
import com.nt.Backend_NT.repositories.InventoryRepository;
import com.nt.Backend_NT.repositories.ProductRepository;

@Service
public class InventoryEntryService {
	@Autowired
	private InventoryEntryRepository inventoryEntryRepository;
	@Autowired
	private InventoryEntryXLabelService inventoryEntryXLabelService;
	@Autowired
	private ProductService productService;
	@Autowired
	private LabelService labelService;
	@Autowired 
	private ProductRepository productRepository;
	@Autowired
	private InventoryRepository inventoryRepository;
	public InventoryEntryEntity createInventoryEntry(InventoryEntryRequest entry)
			throws Exception {
		
		ProductEntity productInDB = productService.getProductByReference(entry.getProducto());
		
		if(entry.getFecha() != null && entry.getHora() != null && isValidLabels(entry.getEtiquetas())) {
			
			if(entry.getCostoxunidad() != productInDB.getCostoxunidad()) {
				productInDB.setCostoxunidad(entry.getCostoxunidad());
				productInDB = productRepository.save(productInDB);
			}
			
			InventoryEntryEntity inventory = new InventoryEntryEntity();
			inventory.setProducto(productInDB);
			inventory.setProveedor(entry.getProveedor());
			inventory.setCostoxunidad(entry.getCostoxunidad());
			inventory.setCantidadtotal(entry.getCantidadTotal());
			inventory.setFecha(entry.getFecha());
			inventory.setHora(entry.getHora());
					
			inventory = inventoryEntryRepository.save(inventory);
			inventoryEntryXLabelService.createInventoryEntryXLabels(inventory, entry.getEtiquetas());
			
			return inventory;
		}
		
		throw new BadRequestException("Debe proporcionar una fecha y hora del ingreso");

	}

	public InventoryEntryEntity updateInventoryEntry(int entryId,InventoryEntryUpdateRequest entry)
			throws Exception {
		InventoryEntryEntity inventoryEntry = inventoryEntryRepository.getById(entryId);
		
		if(inventoryEntry != null) {
			
			if(entry.getFecha() != null && entry.getHora() != null && isValidLabelsToUpdate(inventoryEntry,
					entry.getEtiquetas())) {
				ProductEntity productInDB = inventoryEntry.getProducto();
				if(productInDB.getCostoxunidad() != entry.getCostoxunidad()) {
					productInDB.setCostoxunidad(entry.getCostoxunidad());
					inventoryEntry.setCostoxunidad(entry.getCostoxunidad());

				}
				inventoryEntry.setProveedor(entry.getProveedor());
				inventoryEntry.setFecha(entry.getFecha());
				inventoryEntry.setHora(entry.getHora());
				
				inventoryEntryXLabelService.updateInventoryEntryXLabels(inventoryEntry, entry.getEtiquetas());
					
				return inventoryEntryRepository.save(inventoryEntry);
			}
			
			throw new BadRequestException("Se debe asignar una fecha y hora del ingreso");
			
		}
		
		throw new NotFoundException(String.format("No existe el ingreso con el id %o", entryId));
	}
	
	public InventoryEntryEntity deleteInventoryEntry(int entryId) throws NotFoundException, BadRequestException {
		InventoryEntryEntity entry = inventoryEntryRepository.findById(entryId);
		
		if(entry != null) {
			List<InventoryEntryXLabelEntity> entryxlabels = 
					inventoryEntryXLabelService.getInventoryEntryXLabelsByInventoryEntry(entry);	
			
			if(isValidToDelete(entryxlabels)) {
				for(InventoryEntryXLabelEntity entryLabel : entryxlabels) {
					LabelEntity labelInDB = entryLabel.getEtiqueta();
					InventoryEntity inventoryLabel = inventoryRepository.findByLabelReference(labelInDB);
					int updatedAmount = inventoryLabel.getCantidad() - entryLabel.getCantidad();
					
					inventoryLabel.setCantidad(updatedAmount);
					inventoryRepository.save(inventoryLabel);
				}
				
				inventoryEntryRepository.delete(entry);
				return entry;
			}
			
		}

		throw new NotFoundException(String.format("No existe el ingreso con el id %d",entryId));
	}
	
 	public boolean isValidLabels(List<LabelInventoryRequest> labels) throws Exception {
		for(LabelInventoryRequest label : labels) {
			if(label.getCantidad() < 1 ) {
				throw new Exception(String.format(
						"La cantidad de la etiqueta %d debe ser mayor o igual a 0",
						label.getCantidad()));
			}
			LabelEntity labelInDB = labelService.getLabel(label.getId());
		}
		return true;
	}	

	public boolean isValidLabelsToUpdate(InventoryEntryEntity entry, List<LabelInventoryRequest> labels) throws Exception {
		for(LabelInventoryRequest label : labels) {
			LabelEntity labelInDB = labelService.getLabel(label.getId());
			InventoryEntryXLabelEntity inventoryLabel = inventoryEntryXLabelService
					.getInventoryEntryXLabelsByLabelAndInventoryEntry(entry, labelInDB);
			InventoryEntity inventoryEntity = inventoryRepository.findByLabelReference(labelInDB);
			
			int updatedAmount = inventoryEntity.getCantidad()-inventoryLabel.getCantidad()+label.getCantidad();
			
			if(label.getCantidad() < 0) {
				throw new BadRequestException(String.format(
						"La cantidad de la etiqueta %d debe ser mayor o igual a 0",
						label.getCantidad()));
			}
			
			if(updatedAmount < 0) {
				throw new BadRequestException(String.format(
						"El inventario actual no permite editar el ingreso con la etiqueta %s",
						labelInDB.getNombre()));
			}
		}
		return true;	
	}

	public boolean isValidToDelete(List<InventoryEntryXLabelEntity> entryxlabels) throws BadRequestException {
		
		for(InventoryEntryXLabelEntity entryLabel : entryxlabels) {
			LabelEntity labelInDB = entryLabel.getEtiqueta();
			InventoryEntity inventoryLabel = inventoryRepository.findByLabelReference(labelInDB);
			int updatedAmount = inventoryLabel.getCantidad() - entryLabel.getCantidad();
			
			if(updatedAmount < 0) {
				throw new BadRequestException(String.format(
						"El inventario actual no permite eliminar el ingreso con la etiqueta %s",
						labelInDB.getNombre()));
			}
		}

		return true;
	}
}
