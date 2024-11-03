package com.konsol.divinesign.repository;

import com.konsol.divinesign.domain.ValidationModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ValidationModel entity.
 */
@Repository
public interface ValidationModelRepository extends MongoRepository<ValidationModel, String> {}
