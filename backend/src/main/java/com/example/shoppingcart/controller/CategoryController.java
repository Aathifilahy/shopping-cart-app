package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.Category;
import com.example.shoppingcart.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryService categoryService;

	@GetMapping
	public List<Category> getAll() {
		return categoryService.getAllCategories();
	}

	@GetMapping("/{id}")
	public Category get(@PathVariable String id) {
		return categoryService.getCategory(id);
	}
}
