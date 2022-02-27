package com.nt.Backend_NT.model;

import java.util.List;

import lombok.Data;

@Data
public class InventoryItem {
	private String referencia;
	private String producto;
	private int cantidadTotal;
	private int cantidadEtiquetas;
	private List<LabelInventoryResponse> etiquetas;
}
