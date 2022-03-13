package com.nt.Backend_NT.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Table(name = "estadoprestamo")
@Entity
@Data
public class LoanStatesEntity {
	@Id
	private int id;
	private String estado;
}
