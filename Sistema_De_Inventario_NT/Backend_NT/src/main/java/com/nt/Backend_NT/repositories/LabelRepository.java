package com.nt.Backend_NT.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nt.Backend_NT.entities.LabelEntity;
import com.nt.Backend_NT.entities.ProductEntity;

public interface LabelRepository extends JpaRepository<LabelEntity, Integer> {
	public LabelEntity findByNombreAndProductReference(String nombre,ProductEntity producto);
	@Query(value = "SELECT e.id,e.nombre,e.producto "
			+ "FROM productos p JOIN categorias c on p.categoria = c.id "
			+ "JOIN etiquetas e on p.referencia = e.producto WHERE c.id = ?1",
			nativeQuery = true)
	public List<LabelEntity> findByCategory(int categoryId);
	public List<LabelEntity> findByProductReference(ProductEntity productReference);
	public LabelEntity findById(int labelId);
}
