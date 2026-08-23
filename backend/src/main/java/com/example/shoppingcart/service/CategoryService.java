package com.example.shoppingcart.service;

import com.example.shoppingcart.exception.ResourceNotFoundException;
import com.example.shoppingcart.model.Category;
import com.example.shoppingcart.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryRepository categoryRepository;

	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	public Category getCategory(String id) {
		return categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
	}

	public Category createCategory(Category category) {
		return categoryRepository.save(category);
	}

	public Category updateCategory(String id, Category category) {
		category.setId(id);
		return categoryRepository.save(category);
	}

	public void deleteCategory(String id) {
		categoryRepository.deleteById(id);
	}
}
