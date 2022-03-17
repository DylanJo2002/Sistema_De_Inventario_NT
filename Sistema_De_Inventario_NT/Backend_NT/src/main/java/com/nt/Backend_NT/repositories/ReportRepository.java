package com.nt.Backend_NT.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nt.Backend_NT.entities.ProductReportEntity;

@Repository
public interface ReportRepository extends JpaRepository<ProductReportEntity, String>{
	@Query(value= "SELECT r.referencia, r.nombre , r.descripcion, r.costoxunidad, r.umbral , r.categoria,r.ventas "
			+ "FROM reporteProductos r "
			+ "WHERE r.fecha BETWEEN ?1 AND ?2 AND r.id = ?3 "
			+ "order by r.ventas desc "
			+ "limit ?4",
			nativeQuery = true)
	public List<ProductReportEntity> findProductReportByDatesAndCategory(String dateStart, String dateEnd,
			int categoryId, int top);
	
	@Query(value= "SELECT r.referencia, r.nombre , r.descripcion, r.costoxunidad, r.umbral , r.categoria,r.ventas "
			+ "FROM reporteProductos r "
			+ "WHERE r.fecha BETWEEN ?1 AND ?2 "
			+ "order by r.ventas desc "
			+ "limit ?3",
			nativeQuery = true)
	public List<ProductReportEntity> findProductReportByDates(String dateStart, String dateEnd, int top);
}
