package com.nt.Backend_NT.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.Backend_NT.entities.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
	
	public CategoryEntity findCategoryEntityByNombre(String name);

}
