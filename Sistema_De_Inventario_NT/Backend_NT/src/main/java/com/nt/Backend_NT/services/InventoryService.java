package com.nt.Backend_NT.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nt.Backend_NT.entities.CategoryEntity;
import com.nt.Backend_NT.entities.InventoryEntity;
import com.nt.Backend_NT.entities.LabelEntity;
import com.nt.Backend_NT.entities.ProductEntity;
import com.nt.Backend_NT.model.InventoryRequest;
import com.nt.Backend_NT.model.InventoryResponse;
import com.nt.Backend_NT.model.InventoryItem;
import com.nt.Backend_NT.model.LabelInventoryRequest;
import com.nt.Backend_NT.model.LabelInventoryResponse;
import com.nt.Backend_NT.repositories.InventoryRepository;
import com.nt.Backend_NT.repositories.LabelRepository;
import com.nt.Backend_NT.repositories.ProductRepository;

@Service
public class InventoryService {
	@Autowired
	private InventoryRepository inventoryRepository;
	@Autowired
	private LabelService labelService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private LabelRepository labelRepository;
	@Autowired
	private ProductService productService;
	public InventoryEntity createInventory(LabelInventoryRequest request) throws Exception {
		
		LabelEntity label = labelService.getLabel(request.getId());
		InventoryEntity inventory = new InventoryEntity();
		
		inventory.setLabelReference(label);
		inventory.setCantidad(request.getCantidad());
		return inventoryRepository.save(inventory);		
	}
	
	public List<InventoryEntity> updateInventory(InventoryRequest request) throws Exception {
		
		List<LabelInventoryRequest> labels = request.getEtiquetas();
		List<InventoryEntity> inventories = new ArrayList<InventoryEntity>();
		if(isValidInventoryToUpdate(labels)) {
			
			for(LabelInventoryRequest label : labels) {
				LabelEntity labelInDB = labelRepository.findById(label.getId());
				InventoryEntity inventory = inventoryRepository.findByLabelReference(labelInDB);
				inventory.setCantidad(label.getCantidad());				
				inventoryRepository.save(inventory);
				inventories.add(inventory);
			}
			
		} 

		return inventories;	
	}	
	
	public boolean isValidInventory(List<LabelInventoryRequest> labels) throws Exception {
		for(LabelInventoryRequest label : labels) {
			if(label.getCantidad() < 0 ) {
				throw new Exception(String.format(
						"La cantidad de la etiqueta %d debe ser mayor o igual a 0",
						label.getCantidad()));
			}
			LabelEntity labelInDB = labelService.getLabel(label.getId());
			InventoryEntity inventoryInDB = getInventoryEntity(labelInDB);
			if(inventoryInDB != null) {
				throw new Exception(String.format("Ya existe el inventario de la etiqueta %d",
						label.getId()));
			}
		}
		return true;
	}
	
	public boolean isValidInventoryToUpdate(List<LabelInventoryRequest> labels) throws Exception {
		for(LabelInventoryRequest label : labels) {
			if(label.getCantidad() < 0 ) {
				throw new Exception(String.format(
						"La cantidad de la etiqueta %d debe ser mayor o igual a 0",
						label.getCantidad()));
			}
			LabelEntity labelInDB = labelService.getLabel(label.getId());
		}
		return true;
	}	
	
	public InventoryEntity getInventoryEntity(LabelEntity label) throws Exception {
		InventoryEntity inventoryInBD 
			= inventoryRepository.findByLabelReference(label);
		return inventoryInBD;
	}
	
	public InventoryEntity createInventory(InventoryRequest inventory) throws Exception {
		List<LabelInventoryRequest> labels = inventory.getEtiquetas();
		if(isValidInventory(labels)) {
			for(LabelInventoryRequest label : labels) {
				return createInventory(label);
			}
		}
		return null;
	}

	public InventoryResponse getInventoryByCategory(int category) throws Exception {
		CategoryEntity categoryInDB = categoryService.getCategory(category);
		List<ProductEntity> products;
		if(category == 0) {
			products  = productRepository.findAll();
		}else {
			products  = productRepository.findByCategoriaReference(categoryInDB);
		}
		
		InventoryResponse inventory = new InventoryResponse();
		List<InventoryItem> inventoryItems = new ArrayList<InventoryItem>();
		for(ProductEntity product : products) {
			InventoryItem item = new InventoryItem();
			item.setReferencia(product.getReferencia());
			item.setProducto(product.getNombre());
			item.setCategoria(product.getCategoriaReference().getNombre());
			item.setCantidadTotal(getProductAmount(item.getReferencia()));
			item.setCantidadEtiquetas(getLabelQuantityByProduct(item.getReferencia()));
			item.setEtiquetas(getLabelInventoryResponses(item.getReferencia()));
			inventoryItems.add(item);
		}
		inventory.setInventario(inventoryItems);
		
		return inventory;
	}

	public InventoryResponse getInventoryByReference(String referece) throws Exception {
		ProductEntity productInDB = productService.getProductByReference(referece);
		InventoryItem inventory = new InventoryItem();
		inventory.setReferencia(productInDB.getReferencia());
		inventory.setProducto(productInDB.getNombre());
		inventory.setCategoria(productInDB.getCategoriaReference().getNombre());
		inventory.setCantidadTotal(getProductAmount(productInDB.getReferencia()));
		inventory.setCantidadEtiquetas(getLabelQuantityByProduct(inventory.getReferencia()));
		inventory.setEtiquetas(getLabelInventoryResponses(inventory.getReferencia()));
		InventoryResponse inventoryResponse = new InventoryResponse();
		List<InventoryItem> lista = new ArrayList<InventoryItem>();
		lista.add(inventory);
		inventoryResponse.setInventario(lista);
		return inventoryResponse;
	}
	
 	public int getLabelQuantityByProduct(String reference) {
		return labelRepository.findQuantityByProductReference(reference);
	}
	
	public int getProductAmount(String reference) {
		return productRepository.findProductAmount(reference);
	}

	public List<LabelInventoryResponse> getLabelInventoryResponses(String reference) throws Exception{
		List<InventoryEntity> inventory = inventoryRepository.findByProduct(reference);
		List<LabelEntity> labels = labelService.getLabelsByReference(reference);
		List<LabelInventoryResponse> labelsInventory = new ArrayList<LabelInventoryResponse>();
		Collections.sort(inventory);
		Collections.sort(labels);
		int labelQuantity = getLabelQuantityByProduct(reference);
		for(int i=0;i<labelQuantity;i++) {
			LabelInventoryResponse labelInventory = new LabelInventoryResponse();
			labelInventory.setId(labels.get(i).getId());
			labelInventory.setNombre(labels.get(i).getNombre());
			labelInventory.setCantidad(inventory.get(i).getCantidad());
			labelsInventory.add(labelInventory);
		}
		return labelsInventory;	
	}

	public void deleteInventory(String reference) throws Exception {
		ProductEntity productInBD = productService.getProductByReference(reference);
		
		inventoryRepository.resetInventory(reference);
	}
}
