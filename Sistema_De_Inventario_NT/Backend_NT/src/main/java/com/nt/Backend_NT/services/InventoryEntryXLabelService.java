package com.nt.Backend_NT.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.Backend_NT.entities.InventoryEntity;
import com.nt.Backend_NT.entities.InventoryEntryEntity;
import com.nt.Backend_NT.entities.InventoryEntryXLabelEntity;
import com.nt.Backend_NT.entities.LabelEntity;
import com.nt.Backend_NT.model.LabelInventoryRequest;
import com.nt.Backend_NT.repositories.InventoryEntryXLabelRepository;
import com.nt.Backend_NT.repositories.InventoryRepository;

@Service
public class InventoryEntryXLabelService {
	@Autowired
	private InventoryEntryXLabelRepository inventoryEntryXLabelRepository;
	@Autowired
	private LabelService labelService;
	@Autowired
	private InventoryRepository inventoryRepository;
	
	public List<InventoryEntryXLabelEntity> createInventoryEntryXLabels(InventoryEntryEntity inventoryEntry,
			List<LabelInventoryRequest> labelsEntry) throws Exception {
		
		List<InventoryEntryXLabelEntity> labels = new ArrayList<InventoryEntryXLabelEntity>();
		
		for(LabelInventoryRequest l : labelsEntry) {
			LabelEntity label = labelService.getLabel(l.getId());
			InventoryEntity inventoryEntity = inventoryRepository.findByLabelReference(label);
			InventoryEntryXLabelEntity labelEntry = new InventoryEntryXLabelEntity();
			
			inventoryEntity.setCantidad(inventoryEntity.getCantidad()+l.getCantidad());
			labelEntry.setInventoryEntryReference(inventoryEntry);
			labelEntry.setCantidad(l.getCantidad());
			labelEntry.setEtiqueta(label);
			
			inventoryRepository.save(inventoryEntity);
			labels.add(inventoryEntryXLabelRepository.save(labelEntry));
		};
		
		return labels;
	}
}
