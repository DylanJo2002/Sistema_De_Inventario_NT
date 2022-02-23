package com.nt.Backend_NT.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.nt.Backend_NT.entities.CategoryEntity;
import com.nt.Backend_NT.repositories.CategoryRepository;
import com.nt.Backend_NT.repositories.ProductRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	private ProductService productService;
	@Autowired
	public CategoryService(@Lazy ProductService productService) {
		this.productService = productService;
	}
	
	public CategoryEntity createCategory(CategoryEntity categoryEntity) throws Exception {
		
		CategoryEntity categoryInDB = categoryRepository.findByNombre(
				categoryEntity.getNombre());
		
		if(categoryInDB == null) {
			return categoryRepository.save(categoryEntity);
		}
		
		throw new Exception("La categoría ya existe");
	}
	
	public List<CategoryEntity> getCategories(){
		
			return categoryRepository.findAll();
	}
	
	public CategoryEntity updateCategory(int id, CategoryEntity categoryEntity) 
			throws Exception {
		if(id == 0) {
			throw new Exception("No se puede editar la categoría primaria");
		}
		CategoryEntity categoryInDB = categoryRepository.findById(id);
		
		if(categoryInDB != null) {
			categoryInDB.setNombre(categoryEntity.getNombre());
			return categoryRepository.save(categoryInDB);
		}
		
		throw new Exception("La categoría no existe");
	}	
	
	public CategoryEntity deleteCategory(int id) throws Exception {
		if(id == 0) {
			throw new Exception("No se puede eliminar la categoría primaria");
		}
		CategoryEntity categoryInDB = getCategory(id);
		if(categoryInDB != null) {
			int productsAssocietedCategory = 
					productService.getProductQuantityAssociatedCategory(id);
			if(productsAssocietedCategory == 0) {
				categoryRepository.delete(categoryInDB);
				return categoryInDB;
			}
		}

		throw new Exception(String.format("Existen productos que utilizan la categoría %o",id));
	}	
	
	public CategoryEntity getCategory(int id) throws Exception {
		CategoryEntity category = categoryRepository.findById(id);
		if(category != null) {
			return category;
		}
		
		throw new Exception(String.format("La categoría %o no existe",id));
	}

}