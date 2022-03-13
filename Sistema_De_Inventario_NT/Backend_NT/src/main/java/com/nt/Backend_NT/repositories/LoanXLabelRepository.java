package com.nt.Backend_NT.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.Backend_NT.entities.LoanXLabelEntity;

@Repository
public interface LoanXLabelRepository extends JpaRepository<LoanXLabelEntity, Integer> {

}
