package com.nt.Backend_NT.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class ProductReportEntity {
	@Id
	private String referencia;
	private String nombre;
	private String descripcion;
	private int costoxunidad;
	private int umbral;
	private String categoria;
	private int ventas;
	
}
