package com.nt.Backend_NT.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nt.Backend_NT.entities.SaleEntity;
import com.nt.Backend_NT.model.SaleUpdateRequest;
import com.nt.Backend_NT.model.Sales;
import com.nt.Backend_NT.services.SaleService;


@RestController
@RequestMapping("sales")
public class SaleController {
	
	@Autowired
	private SaleService saleService;
	
	@PostMapping
	public ResponseEntity<SaleEntity> createSale(@RequestBody SaleEntity sale) throws Exception{
		SaleEntity newSale = saleService.createSale(sale);
		return new ResponseEntity<SaleEntity>(newSale,HttpStatus.CREATED);
	}
	
	@PutMapping("/{saleId}")
	public ResponseEntity<SaleEntity> updateSale(@RequestBody SaleUpdateRequest sale,@PathVariable int saleId) 
			throws Exception{
		SaleEntity updatedSale = saleService.updateSale(sale, saleId);
		
		
		return new ResponseEntity<SaleEntity>(updatedSale,HttpStatus.OK);
	}
	
	@DeleteMapping("/{saleId}")
	public ResponseEntity<SaleEntity> deleteSale(@PathVariable int saleId) 
			throws Exception{
		
		SaleEntity deletedSale = saleService.deleteSale(saleId);
		
		return new ResponseEntity<SaleEntity>(deletedSale,HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<Sales> getSaleByIntervalAndCategory(@RequestParam int categoryId
			,@RequestParam String dateStart,@RequestParam String dateEnd) throws Exception{
		Sales sales = saleService.getSalesByCategoryAndDateBetween(categoryId, dateStart, dateEnd);
		return new ResponseEntity<Sales>(sales,HttpStatus.OK);
	}
	
	@GetMapping("/{reference}")
	public ResponseEntity<Sales> getSaleByReferenceAndIntervalAndCategory(@PathVariable String reference,
			@RequestParam String dateStart,@RequestParam String dateEnd) throws Exception{
		Sales sales = saleService.getSalesByProductAndCategoryAndDateBetween(reference, dateStart, dateEnd);
		return new ResponseEntity<Sales>(sales,HttpStatus.OK);
	}
}
