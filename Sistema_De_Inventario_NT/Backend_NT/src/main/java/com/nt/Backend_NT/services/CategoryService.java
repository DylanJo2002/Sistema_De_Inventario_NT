package com.nt.Backend_NT.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.Backend_NT.entities.CategoryEntity;
import com.nt.Backend_NT.repositories.CategoryRepository;
import com.nt.Backend_NT.repositories.ProductRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ProductRepository productRepository;
	
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
		
		CategoryEntity categoryInDB = categoryRepository.findById(id);
		
		if(categoryInDB != null) {
			categoryInDB.setNombre(categoryEntity.getNombre());
			return categoryRepository.save(categoryInDB);
		}
		
		throw new Exception("La categoría no existe");
	}	
	
	public CategoryEntity deleteCategory(int id) throws Exception {
		
		CategoryEntity categoryInDB = categoryRepository.findById(id);
		String message;
		if(categoryInDB != null) {
			int productsAssocietedCategory = 
					productRepository.productsAssocietedCategory(id);
			if(productsAssocietedCategory == 0) {
				categoryRepository.delete(categoryInDB);
				return categoryInDB;
			}
		}
		
		if(categoryInDB == null) {
			message = String.format("La categoría %o no existe",id);
		}else {
			message = String.format("Existen productos que utilizan la categoría %o",id);
		}
		
		throw new Exception(message);
	}	

}
