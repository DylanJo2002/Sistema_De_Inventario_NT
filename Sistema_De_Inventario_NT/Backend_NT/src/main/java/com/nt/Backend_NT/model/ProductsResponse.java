package com.nt.Backend_NT.model;

import java.util.List;

import com.nt.Backend_NT.entities.ProductEntity;

import lombok.Data;

@Data
public class ProductsResponse {
	private List<ProductEntity> products;

	public ProductsResponse(List<ProductEntity> products) {
		super();
		this.products = products;
	}
	
	public ProductsResponse() {
	}
}
