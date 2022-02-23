package com.nt.Backend_NT.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.Backend_NT.entities.CategoryEntity;
import com.nt.Backend_NT.entities.LabelEntity;
import com.nt.Backend_NT.entities.ProductEntity;
import com.nt.Backend_NT.repositories.CategoryRepository;
import com.nt.Backend_NT.repositories.LabelRepository;
import com.nt.Backend_NT.repositories.ProductRepository;

@Service
public class LabelService {
	@Autowired
	private LabelRepository labelRepository;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductService productService;
	
	public LabelEntity createLabel(LabelEntity label) throws Exception {
		ProductEntity productInBD =
				productService.getProductByReference(label.getProducto());
		
		LabelEntity labelInDB = labelRepository.
				findByNombreAndProductReference(label.getNombre(),productInBD);
		
		if(labelInDB == null) {
			label.setProductReference(productInBD);
			labelRepository.save(label);
		}
		
		throw new Exception(String.format("Ya existe la etiqueta %s", label.getNombre()));
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
		
		if(labelId == 0) {
			throw new Exception("No se puede editar la etiqueta primaria");
		}
		
		LabelEntity labelInDB = labelRepository.findById(labelId);
		
		if(labelInDB != null) {
			labelInDB.setNombre(updatedLabel.getNombre());
			labelRepository.save(labelInDB);
			return labelInDB;
		}
		
		
		throw new Exception(String.format("No existe una etiqueta con el id %o",labelId));
		
	}
	
	public LabelEntity deleteLabel(int labelId) throws Exception {
		if(labelId == 0) {
			throw new Exception("No se puede eliminar la etiqueta primaria");
		}
		LabelEntity labelInDB = labelRepository.findById(labelId);
		
		if(labelInDB != null) {
			labelRepository.delete(labelInDB);
			return labelInDB;
		}
		
		throw new Exception(String.format("No existe una etiqueta con el id %o",labelId));			
	}
	
}
