package com.nt.Backend_NT.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nt.Backend_NT.entities.CategoryEntity;
import com.nt.Backend_NT.model.CategoriesResponse;
import com.nt.Backend_NT.services.CategoryService;
import com.nt.Backend_NT.services.ProductService;

@RestController
@RequestMapping("categories")
public class CategoryController {
	
	@Autowired
	CategoryService categoryService;
	
	@GetMapping
	public ResponseEntity<CategoriesResponse> getCategories(){
		CategoriesResponse categoriesResponse = 
				new CategoriesResponse(categoryService.getCategories());
		return new ResponseEntity<CategoriesResponse>(categoriesResponse, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<CategoryEntity> createCategory(@RequestBody CategoryEntity category)
			throws Exception{
		CategoryEntity newCategory = categoryService.createCategory(category);
		
		return new ResponseEntity<CategoryEntity>(newCategory,HttpStatus.CREATED);
	}
	
	@PutMapping("/{categoryId}") 
	public ResponseEntity<CategoryEntity> updateCategory(@PathVariable int categoryId, @RequestBody CategoryEntity category)
			throws Exception{
		CategoryEntity updatedCategory = categoryService.updateCategory(categoryId,category);
		
		return new ResponseEntity<CategoryEntity>(updatedCategory,HttpStatus.OK);
	}
	
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<CategoryEntity> deleteCategory(@PathVariable int categoryId, @RequestBody CategoryEntity category)
			throws Exception{
		CategoryEntity deletedCategory = categoryService.deleteCategory(categoryId);
		return new ResponseEntity<CategoryEntity>(deletedCategory,HttpStatus.OK);
	}

}
