package com.nt.Backend_NT.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "prestamos")
@Data
public class LoanEntity {
	@Id
	private int id;
	@ManyToOne
	@JoinColumn(name="producto")
	private ProductEntity productReference;
	private String titular;
	private String alLocal;
	private int cantidad;
	@ManyToOne
	@JoinColumn(name="estado")
	private LoanStatesEntity estado;
}
