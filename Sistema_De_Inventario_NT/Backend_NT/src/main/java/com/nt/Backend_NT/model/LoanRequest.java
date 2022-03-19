package com.nt.Backend_NT.model;


import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class LoanRequest {
	
	private String producto;
	private String titular;
	private String local;
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@JsonFormat(pattern = "yyyy/MM/dd",timezone = "GTM-5")
	private Date fecha;
	@DateTimeFormat(pattern = "HH:mm")
	@JsonFormat(pattern = "HH:mm")
	private LocalTime hora;
	private List<LabelInventoryRequest> etiquetas;
}
