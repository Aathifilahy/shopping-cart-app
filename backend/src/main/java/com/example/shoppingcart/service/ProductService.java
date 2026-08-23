package com.example.shoppingcart.service;

import com.example.shoppingcart.exception.ResourceNotFoundException;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Product getProduct(String id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
	}

	public List<Product> getProductsByCategory(String categoryId) {
		return productRepository.findByCategoryId(categoryId);
	}

	public Product createProduct(Product product) {
		return productRepository.save(product);
	}

	public Product updateProduct(String id, Product product) {
		product.setId(id);
		return productRepository.save(product);
	}

	public void deleteProduct(String id) {
		productRepository.deleteById(id);
	}
}
