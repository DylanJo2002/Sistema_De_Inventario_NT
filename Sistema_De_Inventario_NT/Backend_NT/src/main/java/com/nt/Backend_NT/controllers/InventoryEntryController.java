package com.nt.Backend_NT.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.Backend_NT.entities.InventoryEntryEntity;
import com.nt.Backend_NT.model.InventoryEntryRequest;
import com.nt.Backend_NT.services.InventoryEntryService;

@RestController
@RequestMapping("inventory-entries")
public class InventoryEntryController {
	
	@Autowired
	private InventoryEntryService inventoryEntryService;
	
	@PostMapping
	public ResponseEntity<InventoryEntryEntity> createInventoryEntry(@RequestBody InventoryEntryRequest entry) throws Exception{
		InventoryEntryEntity inventoryEntry = inventoryEntryService.createInventoryEntry(entry);
		
		return new ResponseEntity<InventoryEntryEntity>(inventoryEntry, HttpStatus.CREATED);
	}
}
