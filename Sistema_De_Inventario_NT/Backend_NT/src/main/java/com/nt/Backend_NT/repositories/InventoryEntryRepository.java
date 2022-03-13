package com.nt.Backend_NT.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.Backend_NT.entities.InventoryEntryEntity;
import com.nt.Backend_NT.entities.ProductEntity;

@Repository
public interface InventoryEntryRepository extends JpaRepository<InventoryEntryEntity, Integer> {
	public InventoryEntryEntity findById(int entryId) ;
	
	public List<InventoryEntryEntity> findByProducto(ProductEntity product);
	
	public List<InventoryEntryEntity> findByProductoAndFechaBetween
	(ProductEntity product, Date fechaStart, Date fechaEnd);
}
