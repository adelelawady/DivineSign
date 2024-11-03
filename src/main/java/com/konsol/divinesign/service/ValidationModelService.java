package com.konsol.divinesign.service;

import com.konsol.divinesign.service.dto.ValidationModelDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.konsol.divinesign.domain.ValidationModel}.
 */
public interface ValidationModelService {
    /**
     * Save a validationModel.
     *
     * @param validationModelDTO the entity to save.
     * @return the persisted entity.
     */
    ValidationModelDTO save(ValidationModelDTO validationModelDTO);

    /**
     * Updates a validationModel.
     *
     * @param validationModelDTO the entity to update.
     * @return the persisted entity.
     */
    ValidationModelDTO update(ValidationModelDTO validationModelDTO);

    /**
     * Partially updates a validationModel.
     *
     * @param validationModelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ValidationModelDTO> partialUpdate(ValidationModelDTO validationModelDTO);

    /**
     * Get all the validationModels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ValidationModelDTO> findAll(Pageable pageable);

    /**
     * Get the "id" validationModel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ValidationModelDTO> findOne(String id);

    /**
     * Delete the "id" validationModel.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
