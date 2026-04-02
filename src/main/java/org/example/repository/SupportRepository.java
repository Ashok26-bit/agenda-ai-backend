package org.example.repository;

import org.example.SupportTicket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SupportRepository extends MongoRepository<SupportTicket, String> {
}