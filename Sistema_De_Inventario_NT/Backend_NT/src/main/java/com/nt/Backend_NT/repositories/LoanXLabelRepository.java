package com.nt.Backend_NT.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.Backend_NT.entities.LabelEntity;
import com.nt.Backend_NT.entities.LoanEntity;
import com.nt.Backend_NT.entities.LoanXLabelEntity;

@Repository
public interface LoanXLabelRepository extends JpaRepository<LoanXLabelEntity, Integer> {
	
	public LoanXLabelEntity findByPrestamoAndEtiqueta(LoanEntity prestamo, LabelEntity etiqueta);
	
	public List<LoanXLabelEntity> findByPrestamo(LoanEntity prestamo);
}
