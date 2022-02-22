package com.nt.Backend_NT.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.Backend_NT.entities.CategoryEntity;
import com.nt.Backend_NT.entities.ProductEntity;
import com.nt.Backend_NT.repositories.CategoryRepository;
import com.nt.Backend_NT.repositories.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	public ProductEntity createProduct(ProductEntity product) throws Exception{
		String message;
		ProductEntity productInBD = 
				productRepository.findByReferencia(product.getReferencia());
		CategoryEntity categoryInDB = 
				categoryRepository.findById(product.getCategoria());
		
		if(productInBD == null && categoryInDB != null) {
			product.setCategoriaReference(categoryInDB);
			return productRepository.save(product);
		}
		
		if(categoryInDB == null) {
			message = String.format("No existe la categoría con el id %o",
					product.getCategoria());
		} else {
			message = String.format("Ya existe un producto con la referencia %s",
					product.getReferencia());
		}
		
		throw new Exception(message);
	}
	
	public List<ProductEntity> getProductsByCategory(int category) {
		String message;
		if(category==0) {
			return productRepository.findAll();
		}
		CategoryEntity categoryInDB = 
				categoryRepository.findById(category);
		
		if(categoryInDB == null) {
			
			message = String.format("No existe la categoría con el id %o",
					category);			
		}
		return productRepository.findByCategoriaReference(categoryInDB);
	}

	public ProductEntity getProductByReference(String reference) throws Exception {
		ProductEntity productInBD = 
				productRepository.findByReferencia(reference);
		
		if(productInBD != null) {
			return productInBD;
		}
		
		throw new Exception(String.format("Ya existe un producto con la referencia %s",
				reference));		
	}
}
