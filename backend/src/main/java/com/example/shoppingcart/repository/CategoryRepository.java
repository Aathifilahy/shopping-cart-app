package com.example.shoppingcart.repository;

import com.example.shoppingcart.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
}
