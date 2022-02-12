package com.nt.Backend_NT.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.Backend_NT.entities.CategoryEntity;
import com.nt.Backend_NT.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	public CategoryEntity createCategory(CategoryEntity categoryEntity) throws Exception {
		
		CategoryEntity categoryInDB = categoryRepository.findCategoryEntityByNombre(
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
		
		CategoryEntity categoryInDB = categoryRepository.findById(id).get();
		
		if(categoryInDB != null) {
			categoryInDB.setNombre(categoryEntity.getNombre());
			return categoryRepository.save(categoryInDB);
		}
		
		throw new Exception("La categoría no existe");
	}	
	
	public CategoryEntity deleteCategory(int id) throws Exception {
		
		CategoryEntity categoryInDB = categoryRepository.findById(id).get();
		
		if(categoryInDB != null) {
			categoryRepository.delete(categoryInDB);
			return categoryInDB;
		}
		
		throw new Exception("La categoría no existe");
	}	

}
