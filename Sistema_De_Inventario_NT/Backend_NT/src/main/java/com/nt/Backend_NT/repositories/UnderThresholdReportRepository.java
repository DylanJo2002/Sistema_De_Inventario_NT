package com.nt.Backend_NT.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nt.Backend_NT.entities.UnderThresholdEntity;

@Repository
public interface UnderThresholdReportRepository extends JpaRepository<UnderThresholdEntity, String>{
	@Query(value= "SELECT r.referencia, r.nombre , r.descripcion, r.costoxunidad, r.umbral , r.categoria,r.cantidad "
			+ "FROM reporteInventario r "
			+ "WHERE r.id = ?1 ", 
			nativeQuery = true)
	public List<UnderThresholdEntity> findInventoryByCategory(int categoryId);
	
	@Query(value= "SELECT r.referencia, r.nombre , r.descripcion, r.costoxunidad, r.umbral , r.categoria,r.cantidad "
			+ "FROM reporteInventario r ",
			nativeQuery = true)
	public List<UnderThresholdEntity> findInventory();
	
}
