package com.konsol.divinesign.service;

import com.konsol.divinesign.domain.Verse;
import com.konsol.divinesign.service.dto.VerseDTO;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.konsol.divinesign.domain.Verse}.
 */
public interface VerseService {
    /**
     * Save a verse.
     *
     * @param verseDTO the entity to save.
     * @return the persisted entity.
     */
    VerseDTO save(VerseDTO verseDTO);

    /**
     * Updates a verse.
     *
     * @param verseDTO the entity to update.
     * @return the persisted entity.
     */
    VerseDTO update(VerseDTO verseDTO);

    /**
     * Partially updates a verse.
     *
     * @param verseDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VerseDTO> partialUpdate(VerseDTO verseDTO);

    /**
     * Get all the verses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VerseDTO> findAll(Pageable pageable);

    /**
     * Get the "id" verse.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VerseDTO> findOne(String id);

    /**
     * Delete the "id" verse.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * searchVersesByWord
     * @param word query word
     * @return list of verses
     */
    List<Verse> searchVersesByWord(String word);

    /***
     * findWordOccurrencesInVerse
     * @param verse verse
     * @param word query word
     * @return number of occurrences of word in verse
     */
    int findWordOccurrencesInVerse(Verse verse, String word);

    int findWordOccurrencesInVerseList(List<Verse> verse, String word);
}
