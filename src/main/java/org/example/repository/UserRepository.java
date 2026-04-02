package org.example.repository;

import org.example.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
    // UserRepository.java
    User findByUsername(String username);
}