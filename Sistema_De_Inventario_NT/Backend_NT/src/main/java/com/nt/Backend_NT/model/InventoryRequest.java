package com.nt.Backend_NT.model;

import java.util.List;

import lombok.Data;

@Data
public class InventoryRequest {
	private List<LabelInventoryRequest> etiquetas;
	private String palabra;
}
