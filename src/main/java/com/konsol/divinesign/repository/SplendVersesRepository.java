package com.konsol.divinesign.repository;

import com.konsol.divinesign.domain.SplendVerses;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the SplendVerses entity.
 */
@Repository
public interface SplendVersesRepository extends MongoRepository<SplendVerses, String> {}
