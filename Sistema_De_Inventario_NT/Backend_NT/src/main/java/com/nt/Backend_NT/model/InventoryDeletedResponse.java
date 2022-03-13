package com.nt.Backend_NT.model;

import lombok.Data;

@Data
public class InventoryDeletedResponse {
	private int etiquetasVaciadas;

	public InventoryDeletedResponse(int etiquetasVaciadas) {
		this.etiquetasVaciadas = etiquetasVaciadas;
	}

	public InventoryDeletedResponse() {
	} 
	
}
