package com.nt.Backend_NT.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.Backend_NT.entities.InventoryEntryEntity;
import com.nt.Backend_NT.entities.InventoryEntryXLabelEntity;
import com.nt.Backend_NT.entities.LabelEntity;

public interface InventoryEntryXLabelRepository extends JpaRepository<InventoryEntryXLabelEntity, Integer>{
	public InventoryEntryXLabelEntity findByInventoryEntryReferenceAndEtiqueta(InventoryEntryEntity entry,LabelEntity label);
}