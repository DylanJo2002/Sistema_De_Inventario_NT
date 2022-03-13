package com.nt.Backend_NT.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="prestamoxlabel")
public class LoanXLabelEntity {
	@Id
	private int id;
	@ManyToOne
	@JoinColumn(name="prestamo")
	private LoanEntity prestamo;
	@ManyToOne
	@JoinColumn(name="etiqueta")
	private LabelEntity etiqueta;
	private int cantidad;
}
