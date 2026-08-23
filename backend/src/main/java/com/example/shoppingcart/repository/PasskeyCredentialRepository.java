package com.example.shoppingcart.repository;

import com.example.shoppingcart.model.PasskeyCredential;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PasskeyCredentialRepository extends MongoRepository<PasskeyCredential, String> {
	Optional<PasskeyCredential> findByCredentialId(String credentialId);
	void deleteByUserId(String userId);
}
