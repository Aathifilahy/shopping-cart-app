package com.example.shoppingcart.repository;

import com.example.shoppingcart.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByAuthProviderAndAuthId(String provider, String authId);
}