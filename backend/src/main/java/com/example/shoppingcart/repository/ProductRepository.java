package com.example.shoppingcart.repository;

import com.example.shoppingcart.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
	List<Product> findByCategoryId(String categoryId);
}
