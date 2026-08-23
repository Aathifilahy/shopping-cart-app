package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;

	@GetMapping
	public List<Product> getAll() {
		return productService.getAllProducts();
	}

	@GetMapping("/{id}")
	public Product get(@PathVariable String id) {
		return productService.getProduct(id);
	}

	@GetMapping("/category/{categoryId}")
	public List<Product> getByCategory(@PathVariable String categoryId) {
		return productService.getProductsByCategory(categoryId);
	}

	@GetMapping("/search")
	public List<Product> search(@RequestParam String q) {
		return List.of();
	}
}
