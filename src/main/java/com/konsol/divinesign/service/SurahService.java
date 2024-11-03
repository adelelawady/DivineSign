package com.konsol.divinesign.service;

import com.konsol.divinesign.service.dto.SurahDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.konsol.divinesign.domain.Surah}.
 */
public interface SurahService {
    /**
     * Save a surah.
     *
     * @param surahDTO the entity to save.
     * @return the persisted entity.
     */
    SurahDTO save(SurahDTO surahDTO);

    /**
     * Updates a surah.
     *
     * @param surahDTO the entity to update.
     * @return the persisted entity.
     */
    SurahDTO update(SurahDTO surahDTO);

    /**
     * Partially updates a surah.
     *
     * @param surahDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SurahDTO> partialUpdate(SurahDTO surahDTO);

    /**
     * Get all the surahs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SurahDTO> findAll(Pageable pageable);

    /**
     * Get the "id" surah.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SurahDTO> findOne(String id);

    /**
     * Delete the "id" surah.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
