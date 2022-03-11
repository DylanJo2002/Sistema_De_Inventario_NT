package com.nt.Backend_NT.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.Backend_NT.controllers.SaleXLabelService;
import com.nt.Backend_NT.entities.InventoryEntity;
import com.nt.Backend_NT.entities.LabelEntity;
import com.nt.Backend_NT.entities.ProductEntity;
import com.nt.Backend_NT.entities.SaleEntity;
import com.nt.Backend_NT.entities.SaleXLabelEntity;
import com.nt.Backend_NT.exceptions.ConflictException;
import com.nt.Backend_NT.exceptions.NotFoundException;
import com.nt.Backend_NT.model.LabelInventoryRequest;
import com.nt.Backend_NT.model.SaleUpdateRequest;
import com.nt.Backend_NT.repositories.InventoryRepository;
import com.nt.Backend_NT.repositories.LabelRepository;
import com.nt.Backend_NT.repositories.SaleRepository;
import com.nt.Backend_NT.repositories.SaleXLabelRepository;

@Service
public class SaleService {
	@Autowired
	private SaleRepository saleRepository;
	@Autowired
	private SaleXLabelRepository saleXLabelRepository;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private InventoryRepository inventoryRepository;
	@Autowired
	private ProductService productService;
	@Autowired
	private SaleXLabelService saleXLabelService;
	@Autowired
	private LabelService labelService;

	public SaleEntity createSale(SaleEntity sale) throws Exception {

		ProductEntity product = productService.getProductByReference(sale.getProducto());

		if (inventoryService.isValidInventoryToUpdate(sale.getEtiquetas())) {
			if (isValidAmountToUpdate(sale.getEtiquetas())) {
				SaleEntity newSale;
				sale.setProductReference(product);
				sale.setCantidadtotal(totalAmount(sale.getEtiquetas()));
				newSale = saleRepository.save(sale);
				saleXLabelService.createSaleXLabel(newSale, sale.getEtiquetas());
				updateInventory(sale.getEtiquetas());
				return newSale;
			}
		}

		return null;
	}

	public SaleEntity updateSale(SaleUpdateRequest sale, int saleId) throws Exception {
		SaleEntity saleEntity = saleRepository.findById(saleId);

		if (saleEntity != null) {
			if (isValidAmountToUpdateSale(saleEntity, sale.getEtiquetas())) {
				saleEntity.setFecha(sale.getFecha());
				saleEntity.setHora(sale.getHora());
				for (LabelInventoryRequest label : sale.getEtiquetas()) {
					LabelEntity labelInDB = labelService.getLabel(label.getId());

					SaleXLabelEntity saleXLabelEntity = saleXLabelRepository.findByVentaAndEtiqueta(saleEntity,
							labelInDB);
					InventoryEntity inventoryEntity = inventoryService.getInventoryEntity(labelInDB);
					int updatedAmount = inventoryEntity.getCantidad() + saleXLabelEntity.getCantidad()
							- label.getCantidad();

					inventoryEntity.setCantidad(updatedAmount);
					saleXLabelEntity.setCantidad(label.getCantidad());

					inventoryRepository.save(inventoryEntity);
					saleXLabelRepository.save(saleXLabelEntity);

				}
				return saleRepository.save(saleEntity);
			}

		}
		throw new NotFoundException(String.format("La venta con el id %o no existe", saleId));
	}

	public int totalAmount(List<LabelInventoryRequest> labels) {
		return labels.stream().mapToInt(l -> l.getCantidad()).sum();
	}

	public boolean isValidAmountToUpdate(List<LabelInventoryRequest> labels) throws Exception {

		for (LabelInventoryRequest label : labels) {
			LabelEntity labelInDB = labelService.getLabel(label.getId());
			InventoryEntity inventory = inventoryRepository.findByLabelReference(labelInDB);

			if (inventory.getCantidad() < label.getCantidad()) {
				throw new ConflictException(String.format(
						"No tiene suficiente inventario de la etiqueta %s para la venta", labelInDB.getNombre()));
			}

		}

		return true;
	}

	public boolean isValidAmountToUpdateSale(SaleEntity sale, List<LabelInventoryRequest> labels) throws Exception {

		for (LabelInventoryRequest label : labels) {
			LabelEntity labelInDB = labelService.getLabel(label.getId());

			SaleXLabelEntity saleXLabelEntity = saleXLabelRepository.findByVentaAndEtiqueta(sale, labelInDB);
			if (saleXLabelEntity != null) {
				InventoryEntity inventoryEntity = inventoryService.getInventoryEntity(labelInDB);
				int updatedAmount = inventoryEntity.getCantidad() + saleXLabelEntity.getCantidad()
						- label.getCantidad();

				if (updatedAmount < 0) {
					throw new ConflictException(String.format(
							"No tiene suficiente inventario de la etiqueta %s para la venta", labelInDB.getNombre()));

				}

			}else {
				throw new NotFoundException(
						String.format("No se encontró la venta para la etiqueta %s", labelInDB.getNombre()));
			}


		}

		return true;
	}

	public void updateInventory(List<LabelInventoryRequest> labels) throws Exception {
		for (LabelInventoryRequest label : labels) {
			LabelEntity labelInDB = labelService.getLabel(label.getId());
			InventoryEntity inventory = inventoryRepository.findByLabelReference(labelInDB);

			inventory.setCantidad(inventory.getCantidad() - label.getCantidad());
			inventoryRepository.save(inventory);

		}
	}

}
