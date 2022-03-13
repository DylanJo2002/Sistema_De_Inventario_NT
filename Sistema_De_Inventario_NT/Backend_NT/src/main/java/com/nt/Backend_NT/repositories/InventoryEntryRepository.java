package com.nt.Backend_NT.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nt.Backend_NT.entities.InventoryEntryEntity;

@Repository
public interface InventoryEntryRepository extends JpaRepository<InventoryEntryEntity, Integer> {
	public InventoryEntryEntity findById(int entryId) ;
}
