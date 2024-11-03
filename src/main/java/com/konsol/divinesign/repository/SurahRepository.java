package com.konsol.divinesign.repository;

import com.konsol.divinesign.domain.Surah;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Surah entity.
 */
@Repository
public interface SurahRepository extends MongoRepository<Surah, String> {}
