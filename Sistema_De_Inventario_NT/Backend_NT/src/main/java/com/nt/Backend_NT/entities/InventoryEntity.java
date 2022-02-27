package com.nt.Backend_NT.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Table(name="inventario")
@Entity
public class InventoryEntity implements Comparable<InventoryEntity>{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@OneToOne
	@JoinColumn(name="etiqueta")
	private LabelEntity labelReference;
	@Transient
	private int etiqueta;
	@Column
	private int cantidad;
	
	@Override
	public int compareTo(InventoryEntity o) {	
		return labelReference.getId()-o.getLabelReference().getId();
	}
}
