package com.nt.Backend_NT.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nt.Backend_NT.entities.InventoryEntity;
import com.nt.Backend_NT.entities.LabelEntity;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, Integer>{
	public InventoryEntity findByLabelReference(LabelEntity label);
	@Query( value = "select i.id, i.etiqueta, i.cantidad from inventario i join etiquetas e on i.etiqueta = e.id "
			+ "where e.producto = ?1", 
			  nativeQuery = true)
	public List<InventoryEntity> findByProduct(String reference);
	@Transactional
	@Modifying
	@Query( value = "update inventario i join etiquetas e on i.etiqueta = e.id set cantidad = 0 "
			+ "where e.producto = ?1",
			  nativeQuery = true)
	public void resetInventory(String reference);
}
