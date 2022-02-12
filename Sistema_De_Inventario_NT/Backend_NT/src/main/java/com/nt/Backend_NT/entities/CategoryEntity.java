package com.nt.Backend_NT.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "categorias")
public class CategoryEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column
	private String nombre;

}
