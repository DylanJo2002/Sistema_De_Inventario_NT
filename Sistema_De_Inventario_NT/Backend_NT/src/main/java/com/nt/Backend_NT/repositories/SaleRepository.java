package com.nt.Backend_NT.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nt.Backend_NT.entities.ProductEntity;
import com.nt.Backend_NT.entities.SaleEntity;

public interface SaleRepository extends JpaRepository<SaleEntity, Integer> {
	public SaleEntity findById(int id);

	@Query(value="SELECT * FROM ventas v JOIN productos p ON v.producto = p.referencia "
			+ "WHERE p.categoria = ?1 AND v.fecha BETWEEN ?2 AND ?3"
			,nativeQuery = true)
	public List<SaleEntity> findByCategoryAndDateBetween(int categoryId, String fInicio, String fFin);

	@Query(value="SELECT * FROM ventas v JOIN productos p ON v.producto = p.referencia "
			+ "WHERE p.categoria = ?1"
			,nativeQuery = true)
	public List<SaleEntity> findByCategory(int categoryId);
	
	public List<SaleEntity> findByFechaBetween(Date fechaTimeStart, Date fechaTimeEnd);

	public List<SaleEntity> findByProductReferenceAndFechaBetween(ProductEntity product,
			Date fechaTimeStart, Date fechaTimeEnd);
	
	public List<SaleEntity> findByProductReference(ProductEntity product);
	
}
