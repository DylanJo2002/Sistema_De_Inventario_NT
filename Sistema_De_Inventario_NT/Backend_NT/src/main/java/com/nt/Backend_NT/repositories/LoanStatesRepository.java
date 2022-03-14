package com.nt.Backend_NT.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.Backend_NT.entities.LoanStatesEntity;

@Repository
public interface LoanStatesRepository extends JpaRepository<LoanStatesEntity, Integer> {
	public LoanStatesEntity findByEstado(String estado);
	public LoanStatesEntity findById(int id);
}
