package com.nt.Backend_NT.model;

import java.util.List;

import com.nt.Backend_NT.entities.CategoryEntity;

import lombok.Data;

@Data
public class CategoriesResponse {
	private List<CategoryEntity> categories;

	public CategoriesResponse(List<CategoryEntity> categoriesEntities) {
		this.categories = categoriesEntities;
	}
	
}
