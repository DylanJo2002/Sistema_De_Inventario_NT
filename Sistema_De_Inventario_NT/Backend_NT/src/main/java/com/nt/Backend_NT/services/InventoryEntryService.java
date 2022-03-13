package com.nt.Backend_NT.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.Backend_NT.entities.InventoryEntryEntity;
import com.nt.Backend_NT.entities.LabelEntity;
import com.nt.Backend_NT.entities.ProductEntity;
import com.nt.Backend_NT.exceptions.BadRequestException;
import com.nt.Backend_NT.model.InventoryEntryRequest;
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
}
