package com.nt.Backend_NT.model;

import java.util.List;

import lombok.Data;

@Data
public class InventoryResponse {
	private List<InventoryItem> inventario;
}
