package com.konsol.divinesign.service;

import com.konsol.divinesign.domain.SplendVariables;
import com.konsol.divinesign.service.dto.SplendVersesDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link SplendVariables}.
 */
public interface SplendVariablesService {
    /**
     * Save a splendVerses.
     *
     * @param splendVersesDTO the entity to save.
     * @return the persisted entity.
     */
    SplendVersesDTO save(SplendVersesDTO splendVersesDTO);

    /**
     * Updates a splendVerses.
     *
     * @param splendVersesDTO the entity to update.
     * @return the persisted entity.
     */
    SplendVersesDTO update(SplendVersesDTO splendVersesDTO);

    /**
     * Partially updates a splendVerses.
     *
     * @param splendVersesDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SplendVersesDTO> partialUpdate(SplendVersesDTO splendVersesDTO);

    /**
     * Get all the splendVerses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SplendVersesDTO> findAll(Pageable pageable);

    /**
     * Get the "id" splendVerses.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SplendVersesDTO> findOne(String id);

    /**
     * Delete the "id" splendVerses.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
