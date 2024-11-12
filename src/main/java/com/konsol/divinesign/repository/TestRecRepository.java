package com.konsol.divinesign.repository;

import com.konsol.divinesign.domain.TestRecord;
import com.konsol.divinesign.domain.Verse;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Verse entity.
 */
@Repository
public interface TestRecRepository extends MongoRepository<TestRecord, String> {
    List<TestRecord> findAllByWord(String word);
}
