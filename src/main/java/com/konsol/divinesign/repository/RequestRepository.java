package com.konsol.divinesign.repository;

import com.konsol.divinesign.domain.Request;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Request entity.
 */
@Repository
public interface RequestRepository extends MongoRepository<Request, String> {}
