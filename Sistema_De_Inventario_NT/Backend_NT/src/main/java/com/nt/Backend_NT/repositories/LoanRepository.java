package com.nt.Backend_NT.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nt.Backend_NT.entities.LoanEntity;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Integer> {
	public LoanEntity findById(int id);
	
	@Query(value = "SELECT * FROM prestamos p JOIN productos pr ON p.producto = pr.referencia "
			+ "WHERE pr.categoria = ?1 AND p.fecha BETWEEN ?2 AND ?3 AND p.estado = ?4",
			nativeQuery = true)
	public List<LoanEntity> findByCategoryAndDatesAndState(int categoryId, String dateStart,
			String dateEnd, int stateId);
	
	@Query(value = "SELECT * FROM prestamos p JOIN productos pr ON p.producto = pr.referencia "
			+ "WHERE pr.categoria = ?1 AND p.fecha BETWEEN ?2 AND ?3",
			nativeQuery = true)
	public List<LoanEntity> findByCategoryAndDates(int categoryId, String dateStart,
			String dateEnd);
	
	@Query(value = "SELECT * FROM prestamos p JOIN productos pr ON p.producto = pr.referencia "
			+ "WHERE pr.categoria = ?1 AND p.estado = ?2",
			nativeQuery = true)
	public List<LoanEntity> findByCategoryAndState(int categoryId, int stateId);

	@Query(value = "SELECT * FROM prestamos p JOIN productos pr ON p.producto = pr.referencia "
			+ "WHERE pr.categoria = ?1",
			nativeQuery = true)
	public List<LoanEntity> findByCategory(int categoryId);

	
	@Query(value = "SELECT * FROM prestamos p JOIN productos pr ON p.producto = pr.referencia "
			+ "WHERE p.fecha BETWEEN ?1 AND ?2 AND p.estado = ?3",
			nativeQuery = true)
	public List<LoanEntity> findByDatesAndState(String dateStart,
			String dateEnd, int stateId);
	
	@Query(value = "SELECT * FROM prestamos p JOIN productos pr ON p.producto = pr.referencia "
			+ "WHERE p.fecha BETWEEN ?1 AND ?2",
			nativeQuery = true)
	public List<LoanEntity> findByDates(String dateStart,
			String dateEnd);
	
	
	@Query(value = "SELECT * FROM prestamos p JOIN productos pr ON p.producto = pr.referencia "
			+ "WHERE p.estado = ?1",
			nativeQuery = true)
	public List<LoanEntity> findByState(int stateId);
	
}	
