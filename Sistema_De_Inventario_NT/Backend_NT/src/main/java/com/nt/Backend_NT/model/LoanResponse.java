package com.nt.Backend_NT.model;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class LoanResponse {
	private int id;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@JsonFormat(pattern = "yyyy/MM/dd")
	private Date fecha;
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm")
	private LocalTime hora;
	private String referencia;
	private String producto;
	private String titular;
	private String local;
	private int cantidad;
	private String estado;
	private List<LabelInventoryResponse> etiquetas;
}
