package com.nt.Backend_NT.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.Backend_NT.entities.SaleEntity;
import com.nt.Backend_NT.entities.SaleXLabelEntity;
import com.nt.Backend_NT.model.LabelInventoryRequest;
import com.nt.Backend_NT.repositories.SaleXLabelRepository;
import com.nt.Backend_NT.services.LabelService;

@Service
public class SaleXLabelService {
	@Autowired
	private SaleXLabelRepository saleXLabelRepository;
	@Autowired
	private LabelService labelService;
	
	public List<SaleXLabelEntity> createSaleXLabel(SaleEntity sale, List<LabelInventoryRequest> labels)
		throws Exception {
		List<SaleXLabelEntity> salesXLabelEntities = new ArrayList<SaleXLabelEntity>();
		
		for(LabelInventoryRequest label : labels) {
			final SaleXLabelEntity saleXLabelEntity = new SaleXLabelEntity();
			saleXLabelEntity.setVenta(sale);
			saleXLabelEntity.setEtiqueta(labelService.getLabel(label.getId()));
			saleXLabelEntity.setCantidad(label.getCantidad());
			salesXLabelEntities.add(saleXLabelRepository.save(saleXLabelEntity));
		}

		
		return salesXLabelEntities;
	}
}
