package com.nt.Backend_NT.jasper.model;


import com.nt.Backend_NT.entities.CategoryEntity;

import lombok.Data;

@Data
public class Product {

	private String reference;
	private String name;
	private String description;
	private int cu;
	private int threshold;
	private String category;
	private int amount;
}
