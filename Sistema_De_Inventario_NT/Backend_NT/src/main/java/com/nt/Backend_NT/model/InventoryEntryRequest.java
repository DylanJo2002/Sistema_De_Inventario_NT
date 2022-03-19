package com.nt.Backend_NT.model;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class InventoryEntryRequest {
	private String producto;
	private String proveedor;
	private int cantidadTotal;
	private int costoxunidad;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@JsonFormat(pattern = "yyyy/MM/dd",timezone = "GMT-5")
	private Date fecha;
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm")
	private LocalTime hora;	
	private List<LabelInventoryRequest> etiquetas;
}
