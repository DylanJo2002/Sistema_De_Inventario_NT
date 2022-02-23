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
	private CategoryService categoryService;
	public ProductEntity createProduct(ProductEntity product) throws Exception{
		String message;
		ProductEntity productInBD = 
				productRepository.findByReferencia(product.getReferencia());
		CategoryEntity categoryInDB = 
				categoryService.getCategory(product.getCategoria());
		
		if(productInBD == null) {
			product.setCategoriaReference(categoryInDB);
			return productRepository.save(product);
		}
		
		throw new Exception(String.format("Ya existe un producto con la referencia %s",
				product.getReferencia()));
	}
	
	public List<ProductEntity> getProductsByCategory(int category) throws Exception {
		String message;
		if(category==0) {
			return productRepository.findAll();
		}
		CategoryEntity categoryInDB = 
				categoryService.getCategory(category);
		
		return productRepository.findByCategoriaReference(categoryInDB);
	}

	public ProductEntity getProductByReference(String reference) throws Exception {
		ProductEntity productInBD = 
				productRepository.findByReferencia(reference);
		
		if(productInBD != null) {
			return productInBD;
		}
		
		throw new Exception(String.format("No existe un producto con la referencia %s",
				reference));		
	}

	public ProductEntity updateProductByReference(String reference, ProductEntity updatedProduct) 
			throws Exception {
		ProductEntity productInBD = 
				productRepository.findByReferencia(reference);
		CategoryEntity categoryInDB = 
				categoryService.getCategory(updatedProduct.getCategoria());
		String message;
		if(productInBD != null && categoryInDB != null) {
			productInBD.setNombre(updatedProduct.getNombre());
			productInBD.setDescripcion(updatedProduct.getDescripcion());
			productInBD.setCostoxunidad(updatedProduct.getCostoxunidad());
			productInBD.setUmbral(updatedProduct.getUmbral());
			productInBD.setCategoriaReference(categoryInDB);
			productRepository.save(productInBD);
			return productInBD;
		}
		
		throw new Exception(String.format("No existe un producto con la referencia %s",
				reference));
	}

	public ProductEntity deleteProductByReference(String reference) throws Exception {
		ProductEntity productInBD = 
				productRepository.findByReferencia(reference);
		
		if(productInBD != null) {
			productRepository.delete(productInBD);
			return productInBD;
		}
		
		throw new Exception(String.format("No existe un producto con la referencia %s",
				reference));		
	}	

	public int getProductQuantityAssociatedCategory(int categoryId) {
		return productRepository.productsAssocietedCategory(categoryId);
	}
}
