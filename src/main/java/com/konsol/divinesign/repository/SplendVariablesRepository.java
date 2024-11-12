package com.konsol.divinesign.repository;

import com.konsol.divinesign.domain.SplendVariables;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the SplendVerses entity.
 */
@Repository
public interface SplendVariablesRepository extends MongoRepository<SplendVariables, String> {
    List<SplendVariables> findBySplendId(String splendId);

    Optional<SplendVariables> findByIdAndSplendId(String id, String splendId);
}
