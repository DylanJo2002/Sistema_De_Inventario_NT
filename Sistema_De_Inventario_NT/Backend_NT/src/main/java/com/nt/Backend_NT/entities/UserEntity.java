package com.nt.Backend_NT.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="usuarios")
@Data
public class UserEntity {
	@Id
	private int id;
	@Column
	private String usuario;
	@Column
	private String clave;	
}
