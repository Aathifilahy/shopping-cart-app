package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.Category;
import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.service.CategoryService;
import com.example.shoppingcart.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	private final CategoryService categoryService;
	private final ProductService productService;

	@PostMapping("/categories")
	public Category createCategory(@Valid @RequestBody Category category) {
		return categoryService.createCategory(category);
	}

	@PutMapping("/categories/{id}")
	public Category updateCategory(@PathVariable String id, @Valid @RequestBody Category category) {
		return categoryService.updateCategory(id, category);
	}

	@DeleteMapping("/categories/{id}")
	public void deleteCategory(@PathVariable String id) {
		categoryService.deleteCategory(id);
	}

	@PostMapping("/products")
	public Product createProduct(@Valid @RequestBody Product product) {
		return productService.createProduct(product);
	}

	@PutMapping("/products/{id}")
	public Product updateProduct(@PathVariable String id, @Valid @RequestBody Product product) {
		return productService.updateProduct(id, product);
	}

	@DeleteMapping("/products/{id}")
	public void deleteProduct(@PathVariable String id) {
		productService.deleteProduct(id);
	}
}
