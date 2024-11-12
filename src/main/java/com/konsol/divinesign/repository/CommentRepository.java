package com.konsol.divinesign.repository;

import com.konsol.divinesign.domain.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Comment entity.
 */
@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    @Query("{}")
    Page<Comment> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    List<Comment> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Optional<Comment> findOneWithEagerRelationships(String id);

    List<Comment> findAllBySplendId(String splendId);
}
