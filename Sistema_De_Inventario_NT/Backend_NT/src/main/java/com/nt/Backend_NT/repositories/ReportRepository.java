package com.nt.Backend_NT.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nt.Backend_NT.entities.ProductReportEntity;

@Repository
public interface ReportRepository extends JpaRepository<ProductReportEntity, String>{
	@Query(value= "SELECT p.referencia, ventasT.producto, p.descripcion, p.costoxunidad, p.umbral , c.nombre as categoria, ventasT.ventas FROM "
			+ "(SELECT v.producto, v.fecha, sum(v.cantidadTotal) as ventas FROM ventas v "
			+ "Group by v.producto ) as ventasT join productos p on ventasT.producto = p.referencia "
			+ "join categorias c on c.id = p.categoria "
			+ "WHERE ventasT.fecha BETWEEN ?1 AND ?2 AND c.id = ?3 "
			+ "order by ventasT.ventas desc "
			+ "limit ?4",
			nativeQuery = true)
	public List<ProductReportEntity> findProductReportByDatesAndCategory(String dateStart, String dateEnd,
			int categoryId, int top);
}
