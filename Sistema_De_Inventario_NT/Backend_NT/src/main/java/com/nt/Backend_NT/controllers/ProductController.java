package com.nt.Backend_NT.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nt.Backend_NT.entities.ProductEntity;
import com.nt.Backend_NT.model.ProductsResponse;
import com.nt.Backend_NT.services.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;

	
	@GetMapping
	public ResponseEntity<ProductsResponse> getProductsByCategory(@RequestParam int categoryId)
			throws Exception{
		ProductsResponse response = new ProductsResponse(
				productService.getProductsByCategory(categoryId));
		return new ResponseEntity<ProductsResponse>(response, HttpStatus.OK);
	}

	@GetMapping("/{reference}")
	public ResponseEntity<ProductEntity> getProductsByReference(@PathVariable String reference)
			throws Exception{
		ProductEntity response = productService.getProductByReference(reference);
		return new ResponseEntity<ProductEntity>(response, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<ProductEntity> createProduct(@RequestBody ProductEntity product)
			throws Exception{
		ProductEntity response = productService.createProduct(product);
		return new ResponseEntity<ProductEntity>(response, HttpStatus.CREATED);
	}	
	
	@PutMapping("/{reference}")
	public ResponseEntity<ProductEntity> updateProduct(@PathVariable String reference, 
			@RequestBody ProductEntity product) throws Exception{
		
		ProductEntity updatedProduct = productService.updateProductByReference(reference, product);
		return new ResponseEntity<ProductEntity>(updatedProduct, HttpStatus.OK);
	}
	
	@DeleteMapping("/{reference}")
	public ResponseEntity<ProductEntity> deleteProduct(@PathVariable String reference)
			throws Exception{
		
		ProductEntity deletedProduct = productService.deleteProductByReference(reference);
		return new ResponseEntity<ProductEntity>(deletedProduct, HttpStatus.OK);
	}	
}
