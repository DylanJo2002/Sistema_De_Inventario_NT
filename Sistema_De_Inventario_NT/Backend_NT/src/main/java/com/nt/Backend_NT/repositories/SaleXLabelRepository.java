package com.nt.Backend_NT.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.Backend_NT.entities.SaleXLabelEntity;

@Repository
public interface SaleXLabelRepository extends JpaRepository<SaleXLabelEntity, Integer> {

}
