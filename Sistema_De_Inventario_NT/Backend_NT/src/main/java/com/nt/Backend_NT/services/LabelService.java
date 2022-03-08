package com.nt.Backend_NT.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.Conflict;

import com.nt.Backend_NT.entities.CategoryEntity;
import com.nt.Backend_NT.entities.InventoryEntity;
import com.nt.Backend_NT.entities.LabelEntity;
import com.nt.Backend_NT.entities.ProductEntity;
import com.nt.Backend_NT.exceptions.ConflictException;
import com.nt.Backend_NT.repositories.CategoryRepository;
import com.nt.Backend_NT.repositories.InventoryRepository;
import com.nt.Backend_NT.repositories.LabelRepository;
import com.nt.Backend_NT.repositories.ProductRepository;

@Service
public class LabelService {
	@Autowired
	private LabelRepository labelRepository;
	@Autowired
	private CategoryService categoryService;
	private ProductService productService;
	@Autowired
	private InventoryRepository inventoryRepository;
		
	@Autowired
	public void setProductService(@Lazy ProductService productService) {
		this.productService = productService;
	}

	public LabelEntity createLabel(LabelEntity label) throws Exception {
		if(label.getNombre().equals("NINGUNA")) {
			throw new Exception("No puede crear una etiqueta llamada \"NINGUNA\" ");
		}
		ProductEntity productInBD =
				productService.getProductByReference(label.getProducto());
		
		LabelEntity labelInDB = labelRepository.
				findByNombreAndProductReference(label.getNombre(),productInBD);
		
		if(labelInDB == null) {
			label.setProductReference(productInBD);
			LabelEntity newLabel = labelRepository.save(label);
			InventoryEntity inventory = new InventoryEntity();
			inventory.setCantidad(0);
			inventory.setLabelReference(newLabel);
			inventoryRepository.save(inventory);
			return newLabel;
		}
		
		throw new ConflictException(String.format("Ya existe la etiqueta %s", label.getNombre()));
	}
	
	public List<LabelEntity> getLabelsByCategory(int categoryId) throws Exception{
		if(categoryId == 0) {
			return labelRepository.findAll();
		}
		CategoryEntity categoryEntity = categoryService.getCategory(categoryId);
		
		if(categoryEntity != null) {
			return labelRepository.findByCategory(categoryId);
		}
		return null;
	}
	
	public List<LabelEntity> getLabelsByReference(String reference) throws Exception {
		ProductEntity productInBD = productService.getProductByReference(reference);
		
		if(productInBD != null) {
			return labelRepository.findByProductReference(productInBD);
		}
		
		throw new Exception(String.format("No existe el producto con la referencia %s", reference));
	}
	
	public LabelEntity updateLabel(int labelId, LabelEntity updatedLabel) 
			throws Exception {
			
		LabelEntity labelInDB = labelRepository.findById(labelId);
		if(labelInDB != null) {
			LabelEntity labelInDBSameName = 
					labelRepository.findByNombreAndProductReference(updatedLabel.getNombre()
							, labelInDB.getProductReference());

			if(!labelInDB.getNombre().equals("NINGUNA")) {
				if(labelInDBSameName == null) {
					labelInDB.setNombre(updatedLabel.getNombre());
					labelRepository.save(labelInDB);
					return labelInDB;
				}
				
				throw new ConflictException("Ya existe la etiqueta "+labelInDB.getNombre()
						+" para el producto "+labelInDB.getProductReference().getReferencia());
			}
			throw new Exception("No se puede editar la etiqueta primaria");
		}
		
		
		throw new Exception(String.format("No existe una etiqueta con el id %o",labelId));
		
	}
	
	public LabelEntity getLabel(int labelId) throws Exception {
		LabelEntity labelInDB = labelRepository.findById(labelId);
		
		if(labelInDB != null) {
			return labelInDB;
		}
		
		throw new Exception(String.format("No existe una etiqueta con el id %o",labelId));			
	}	
	public LabelEntity deleteLabel(int labelId) throws Exception {
		LabelEntity labelInDB = labelRepository.findById(labelId);
		
		if(labelInDB != null) {

			if(!labelInDB.getNombre().equals("NINGUNA")) {
				LabelEntity labelNinguna = labelRepository.
						findByNombreAndProductReference("NINGUNA",labelInDB.getProductReference());
				InventoryEntity inventoryNinguna = inventoryRepository.
						findByLabelReference(labelNinguna);
				InventoryEntity inventoryLabel = inventoryRepository.
						findByLabelReference(labelInDB);
				
				inventoryNinguna.setCantidad(
						inventoryNinguna.getCantidad()+inventoryLabel.getCantidad());
				
				labelRepository.delete(labelInDB);
				inventoryRepository.save(inventoryNinguna);
				return labelInDB;
			}
			throw new Exception("No se puede eliminar la etiqueta primaria");
		}
		
		throw new Exception(String.format("No existe una etiqueta con el id %n",labelId));			
	}
	
}
