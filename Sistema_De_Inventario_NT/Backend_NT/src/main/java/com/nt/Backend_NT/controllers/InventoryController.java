package com.nt.Backend_NT.controllers;

import java.util.List;

import javax.websocket.server.PathParam;

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

import com.nt.Backend_NT.entities.InventoryDeletedResponse;
import com.nt.Backend_NT.entities.InventoryEntity;
import com.nt.Backend_NT.model.InventoryItem;
import com.nt.Backend_NT.model.InventoryRequest;
import com.nt.Backend_NT.model.InventoryResponse;
import com.nt.Backend_NT.services.InventoryService;


@RestController
@RequestMapping("/inventory")
public class InventoryController {
	@Autowired
	InventoryService inventoryService;
	
	@GetMapping("/categories")
	public ResponseEntity<InventoryResponse> getInventoryByCategory(@RequestParam int categoryId) 
			throws Exception{
		InventoryResponse inventory = inventoryService.getInventoryByCategory(categoryId);
		return new ResponseEntity<InventoryResponse>(inventory,HttpStatus.OK);
	}
	@GetMapping("/reference")
	public ResponseEntity<InventoryItem> getInventoryByReference(@RequestParam String reference) 
			throws Exception{
		InventoryItem inventory = inventoryService.getInventoryByReference(reference);
		return new ResponseEntity<InventoryItem>(inventory,HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<InventoryEntity> createInventory(@RequestBody InventoryRequest request) throws Exception{
		InventoryEntity inventory = inventoryService.createInventory(request);
		return new ResponseEntity<InventoryEntity>(inventory,HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<List<InventoryEntity>> updateInventory(@RequestBody InventoryRequest request) throws Exception{
		
		List<InventoryEntity> inventory = inventoryService.updateInventory(request);
		return new ResponseEntity<List<InventoryEntity>>(inventory,HttpStatus.OK);
	}
	
	@DeleteMapping("/{reference}")
	public void deleteInventory(@PathVariable String reference) 
			throws Exception{
		inventoryService.deleteInventory(reference);
	}
	
}
