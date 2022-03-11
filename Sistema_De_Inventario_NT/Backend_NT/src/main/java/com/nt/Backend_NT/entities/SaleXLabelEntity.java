package com.nt.Backend_NT.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="ventasxetiquetas")
public class SaleXLabelEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name ="venta")
	private SaleEntity venta;
	@ManyToOne
	@JoinColumn(name ="etiqueta")
	private LabelEntity etiqueta;
	private int cantidad;
}
