package com.nt.Backend_NT.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Table(name = "productos")
@Data
public class ProductEntity {

	@Id
	private String referencia;
	@Column
	private String nombre;
	@Column
	private String descripcion;
	@Column
	private int costoxunidad;
	@Column
	private int umbral;
	
	@ManyToOne
	@JoinColumn(name="categoria")
	private CategoryEntity categoriaReference;
	@Transient
	private int categoria;
}
