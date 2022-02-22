package com.nt.Backend_NT.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nt.Backend_NT.entities.CategoryEntity;
import com.nt.Backend_NT.entities.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String>{
	
	public ProductEntity findByReferencia(String reference);
	public List<ProductEntity> findByCategoriaReference(CategoryEntity category);
	public List<ProductEntity> findAll();
	@Query( value = "SELECT count(*) FROM productos p WHERE p.categoria = ?1", 
			  nativeQuery = true)
	public int productsAssocietedCategory(int categoryId);
}
