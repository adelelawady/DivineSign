package com.konsol.divinesign.service;

import com.konsol.divinesign.service.dto.SplendDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.konsol.divinesign.domain.Splend}.
 */
public interface SplendService {
    /**
     * Save a splend.
     *
     * @param splendDTO the entity to save.
     * @return the persisted entity.
     */
    SplendDTO save(SplendDTO splendDTO);

    /**
     * Updates a splend.
     *
     * @param splendDTO the entity to update.
     * @return the persisted entity.
     */
    SplendDTO update(SplendDTO splendDTO);

    /**
     * Partially updates a splend.
     *
     * @param splendDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SplendDTO> partialUpdate(SplendDTO splendDTO);

    /**
     * Get all the splends.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SplendDTO> findAll(Pageable pageable);

    /**
     * Get all the splends with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SplendDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" splend.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SplendDTO> findOne(String id);

    /**
     * Delete the "id" splend.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
