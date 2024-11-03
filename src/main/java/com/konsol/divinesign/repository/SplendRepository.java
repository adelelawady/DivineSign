package com.konsol.divinesign.repository;

import com.konsol.divinesign.domain.Splend;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Splend entity.
 */
@Repository
public interface SplendRepository extends MongoRepository<Splend, String> {
    @Query("{}")
    Page<Splend> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Splend> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Splend> findOneWithEagerRelationships(String id);
}
