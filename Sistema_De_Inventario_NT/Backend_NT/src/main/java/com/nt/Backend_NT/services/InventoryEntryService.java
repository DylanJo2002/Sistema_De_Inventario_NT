package com.nt.Backend_NT.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.Backend_NT.entities.CategoryEntity;
import com.nt.Backend_NT.entities.InventoryEntity;
import com.nt.Backend_NT.entities.InventoryEntryEntity;
import com.nt.Backend_NT.entities.InventoryEntryXLabelEntity;
import com.nt.Backend_NT.entities.LabelEntity;
import com.nt.Backend_NT.entities.ProductEntity;
import com.nt.Backend_NT.exceptions.BadRequestException;
import com.nt.Backend_NT.exceptions.NotFoundException;
import com.nt.Backend_NT.model.InventoriesEntryResponse;
import com.nt.Backend_NT.model.InventoryEntryRequest;
import com.nt.Backend_NT.model.InventoryEntryResponse;
import com.nt.Backend_NT.model.InventoryEntryUpdateRequest;
import com.nt.Backend_NT.model.LabelInventoryRequest;
import com.nt.Backend_NT.model.LabelInventoryResponse;
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
	@Autowired
	private CategoryService categoryService;
	

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
			inventory.setCantidadtotal(totalAmount(entry.getEtiquetas()));
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
				inventoryEntry.setCantidadtotal(totalAmount(entry.getEtiquetas()));
				
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
	
 	public InventoriesEntryResponse getEntryByProductAndDate(String reference, String dateStart, String dateEnd)
 			throws Exception {
 		
 		ProductEntity productEntity = productService.getProductByReference(reference);
 		
 		
 		
 		if(dateStart.isBlank() && dateStart.isBlank()) {
 			List<InventoryEntryEntity> inventoriesEntry = inventoryEntryRepository
 					.findByProducto(productEntity);
 		
 			return mappeToInventoriesEntryResponse(inventoriesEntry);
 		}
 		
 		if(!dateStart.isBlank() &&  !dateStart.isBlank()) {
 			
			Date dateS = new SimpleDateFormat("yyyy-MM-dd").parse(dateStart);  
			Date dateE = new SimpleDateFormat("yyyy-MM-dd").parse(dateEnd);
			
 			List<InventoryEntryEntity> inventoriesEntry = inventoryEntryRepository
 					.findByProductoAndFechaBetween(productEntity,dateS, dateE);

			
 			return mappeToInventoriesEntryResponse(inventoriesEntry);
 		}
 		
 		throw new BadRequestException("Debe proporcionar una fecha inicio y una "
 				.concat("fecha fin para la búsqueda de ingresos entre fechas,"));

 	}

 	public InventoriesEntryResponse getEntryByCategoryAndDate(int categoryId, String dateStart, String dateEnd)
 			throws Exception {
 		
 		CategoryEntity categoryInDB = categoryService.getCategory(categoryId);
 		
 		if(dateStart.isBlank() && dateStart.isBlank()) {
 			
 			if(categoryId != 0) {
 				
 				List<InventoryEntryEntity> inventoriesEntry = 
 						inventoryEntryRepository.findByCategory(categoryId);
 				
 				return mappeToInventoriesEntryResponse(inventoriesEntry);
 				
 				
 			}else {
 				
 				List<InventoryEntryEntity> inventoriesEntry =
 						inventoryEntryRepository.findAll();
 				
 				return mappeToInventoriesEntryResponse(inventoriesEntry);
 			}
 			
 		}
 		
 		if(!dateStart.isBlank() &&  !dateStart.isBlank()) {
 			
			Date dateS = new SimpleDateFormat("yyyy-MM-dd").parse(dateStart);  
			Date dateE = new SimpleDateFormat("yyyy-MM-dd").parse(dateEnd);
			
			if(categoryId != 0) {
				
				List<InventoryEntryEntity> inventoriesEntry = 
						inventoryEntryRepository.findByCategoryAndDates(categoryId
								, dateStart, dateEnd);
				return mappeToInventoriesEntryResponse(inventoriesEntry);
			}else {
				
				List<InventoryEntryEntity> inventoriesEntry =
						inventoryEntryRepository.findByFechaBetween(dateS, dateE);
				return mappeToInventoriesEntryResponse(inventoriesEntry);
			}

 		}
 		
 		throw new BadRequestException("Debe proporcionar una fecha inicio y una "
 				.concat("fecha fin para la búsqueda de ingresos entre fechas,"));
 	}
 	
 	
 	public InventoriesEntryResponse mappeToInventoriesEntryResponse(List<InventoryEntryEntity> inventoriesEntry) {
 		
 		InventoriesEntryResponse response = new InventoriesEntryResponse();
 		List<InventoryEntryResponse> inventoryEntryList = new ArrayList<InventoryEntryResponse>();
 		
 		for(InventoryEntryEntity entry :inventoriesEntry) {
 			InventoryEntryResponse entryResponse = new InventoryEntryResponse();
 			ProductEntity productInDB = entry.getProducto();
 			entryResponse.setId(entry.getId());
 			entryResponse.setReferencia(productInDB.getReferencia());
 			entryResponse.setProducto(productInDB.getNombre());
 			entryResponse.setProveedor(entry.getProveedor());
 			entryResponse.setCantidadTotal(entry.getCantidadtotal());
 			entryResponse.setCostoxunidad(entry.getCostoxunidad());
 			entryResponse.setFecha(entry.getFecha());
 			entryResponse.setHora(entry.getHora());
 			
 			entryResponse.setEtiquetas(mappetToLabelInventoryResponses(entry));
 			
 			inventoryEntryList.add(entryResponse);
 		}
 		
 		response.setIngresos(inventoryEntryList); 		
 		return response;
 	}
 	
 	public List<LabelInventoryResponse> mappetToLabelInventoryResponses(InventoryEntryEntity entry){
 		
 		List<LabelInventoryResponse> mappedTo = new ArrayList<LabelInventoryResponse>();
 		List<InventoryEntryXLabelEntity> entriesXLabels = 
 				inventoryEntryXLabelService .getInventoryEntryXLabelsByInventoryEntry(entry);
 		
 		entriesXLabels.forEach(l -> {
 			LabelInventoryResponse labelEntryResponse = new LabelInventoryResponse();
 			
 			labelEntryResponse.setId(l.getEtiqueta().getId());
 			labelEntryResponse.setNombre(l.getEtiqueta().getNombre());
 			labelEntryResponse.setCantidad(l.getCantidad());
 			
 			mappedTo.add(labelEntryResponse);
 			
 		});
 		
 		return mappedTo;
 	}
 	
	public boolean isValidLabels(List<LabelInventoryRequest> labels) throws Exception {
		for(LabelInventoryRequest label : labels) {
			if(label.getCantidad() <  0) {
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

	public int totalAmount(List<LabelInventoryRequest> labels) {
		return labels.stream().mapToInt(l -> l.getCantidad()).sum();
	}


}
