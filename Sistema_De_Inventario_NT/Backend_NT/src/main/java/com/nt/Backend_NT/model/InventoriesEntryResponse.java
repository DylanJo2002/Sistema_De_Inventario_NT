package com.nt.Backend_NT.model;

import java.util.List;

import lombok.Data;

@Data
public class InventoriesEntryResponse {
	private List<InventoryEntryResponse> ingresos;
}
