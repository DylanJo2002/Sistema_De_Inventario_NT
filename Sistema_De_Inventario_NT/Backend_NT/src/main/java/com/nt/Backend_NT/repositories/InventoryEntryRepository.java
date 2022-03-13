package com.nt.Backend_NT.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nt.Backend_NT.entities.InventoryEntryEntity;
import com.nt.Backend_NT.entities.ProductEntity;

@Repository
public interface InventoryEntryRepository extends JpaRepository<InventoryEntryEntity, Integer> {
	public InventoryEntryEntity findById(int entryId) ;
	
	public List<InventoryEntryEntity> findByProducto(ProductEntity product);
	
	public List<InventoryEntryEntity> findByProductoAndFechaBetween
	(ProductEntity product, Date fechaStart, Date fechaEnd);

	@Query(value = "SELECT * FROM ingresos i JOIN productos p ON i.producto = p.referencia "
			+ "WHERE p.categoria = ?1" 
			,nativeQuery = true)
	public List<InventoryEntryEntity> findByCategory(int categoryId);

	@Query(value = "SELECT * FROM ingresos i JOIN productos p ON i.producto = p.referencia "
			+ "WHERE p.categoria = ?1 AND i.fecha BETWEEN ?2 AND ?3" 
			,nativeQuery = true)
	public List<InventoryEntryEntity> findByCategoryAndDates(int categoryId, String dateStart,
			String dateEnd);
	
	public List<InventoryEntryEntity> findByFechaBetween(Date fechaStart, Date fechaEnd);

}
