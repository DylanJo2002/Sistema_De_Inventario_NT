package com.nt.Backend_NT.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.Backend_NT.entities.CategoryEntity;
import com.nt.Backend_NT.entities.InventoryEntity;
import com.nt.Backend_NT.entities.LabelEntity;
import com.nt.Backend_NT.entities.ProductEntity;
import com.nt.Backend_NT.entities.SaleEntity;
import com.nt.Backend_NT.entities.SaleXLabelEntity;
import com.nt.Backend_NT.exceptions.BadRequestException;
import com.nt.Backend_NT.exceptions.ConflictException;
import com.nt.Backend_NT.exceptions.NotFoundException;
import com.nt.Backend_NT.model.LabelInventoryRequest;
import com.nt.Backend_NT.model.LabelInventoryResponse;
import com.nt.Backend_NT.model.Sale;
import com.nt.Backend_NT.model.SaleUpdateRequest;
import com.nt.Backend_NT.model.SaleXLabelRequest;
import com.nt.Backend_NT.model.Sales;
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
	@Autowired
	private CategoryService categoryService;
	

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
				saleEntity.setCantidadtotal(totalAmount(sale.getEtiquetas()));
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

	public SaleEntity deleteSale(int saleId) throws Exception {
		SaleEntity saleEntity = saleRepository.findById(saleId);

		if (saleEntity != null) {
			List<SaleXLabelEntity> salesXLabelEntities =
					saleXLabelRepository.findByVenta(saleEntity);
				for (SaleXLabelEntity label : salesXLabelEntities) {

					InventoryEntity inventoryEntity = inventoryService.getInventoryEntity(
							label.getEtiqueta());
					
					int updatedAmount = inventoryEntity.getCantidad() + label.getCantidad();

					inventoryEntity.setCantidad(updatedAmount);

					inventoryRepository.save(inventoryEntity);
					saleXLabelRepository.delete(label);
				}
				saleRepository.delete(saleEntity);
			
				return saleEntity;
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

	public Sales getSalesByCategoryAndDateBetween(int categoryId, String startD, String endD)
			throws Exception{
		
		CategoryEntity categoryInD = categoryService.getCategory(categoryId);
		
		if(startD.isBlank() && endD.isBlank()) {
			List<SaleEntity> sales;
			if(categoryId != 0) {
				sales = saleRepository.findByCategory(categoryId);
			}else {
	
				sales = saleRepository.findAll();
			}
			return mapSalesXLabels(sales);
		}
		
		if(!startD.isBlank() && !endD.isBlank()) {
			List<SaleEntity>  sales;
			
			if(categoryId != 0) {
				sales = saleRepository.findByCategoryAndDateBetween(categoryId,
						startD, endD);	
			}else {
				Date dateS = new SimpleDateFormat("yyyy-MM-dd").parse(startD);  
				Date dateE = new SimpleDateFormat("yyyy-MM-dd").parse(endD);
				
				sales = saleRepository.findByFechaBetween(dateS, dateE);
			}
		
			return mapSalesXLabels(sales);
		}
		
		throw new BadRequestException("Para la búsqueda de ventas entre fechas determinadas debe indicarlas.");
	}

	public Sales mapSalesXLabels(List<SaleEntity> sales){
		Sales mappedTo = new Sales();
		List<Sale> salesList = new ArrayList<Sale>();
		sales.forEach(s -> {
			Sale sale = new Sale();
			List<SaleXLabelEntity> salesXLabel = saleXLabelService.getSalesXLabelBySales(s);
			List<LabelInventoryResponse> salesInventory = new ArrayList<LabelInventoryResponse>();
			sale.setId(s.getId());
			sale.setFecha(s.getFecha());
			sale.setHora(s.getHora());
			sale.setProducto(s.getProductReference().getNombre());
			sale.setReferencia(s.getProductReference().getReferencia());
			sale.setCantidadTotal(s.getCantidadtotal());
			salesXLabel.forEach(label -> {
				LabelInventoryResponse labelInventory = new LabelInventoryResponse();
				labelInventory.setId(label.getEtiqueta().getId());
				labelInventory.setNombre(label.getEtiqueta().getNombre());
				labelInventory.setCantidad(label.getCantidad());
				salesInventory.add(labelInventory);
			});
			
			sale.setEtiquetas(salesInventory);
			salesList.add(sale);
		});
		
		mappedTo.setVentas(salesList);
		
		return mappedTo;
	}

	public Sales getSalesByProductAndCategoryAndDateBetween(String reference
			, String startD, String endD) throws Exception {
		ProductEntity productInD = productService.getProductByReference(reference);
		
		if(startD.isBlank() && endD.isBlank()) {
			List<SaleEntity> sales;
			sales = saleRepository.findByProductReference(productInD);

			return mapSalesXLabels(sales);
		}
		
		if(!startD.isBlank() && !endD.isBlank()) {
			List<SaleEntity>  sales;
			Date dateS = new SimpleDateFormat("yyyy-MM-dd").parse(startD);  
			Date dateE = new SimpleDateFormat("yyyy-MM-dd").parse(endD);
			
			sales = saleRepository.findByProductReferenceAndFechaBetween(productInD, dateS, dateE);
			return mapSalesXLabels(sales);
		}
		
		throw new BadRequestException("Para la búsqueda de ventas entre fechas determinadas debe indicarlas.");		
	}
}
