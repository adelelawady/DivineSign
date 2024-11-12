package com.konsol.divinesign.repository;

import com.konsol.divinesign.domain.Tag;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Tag entity.
 */
@Repository
public interface TagRepository extends MongoRepository<Tag, String> {
    Optional<Tag> findByTitle(String tag);
}
