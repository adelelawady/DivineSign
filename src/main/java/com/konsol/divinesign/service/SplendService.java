package com.konsol.divinesign.service;

import com.konsol.divinesign.domain.Comment;
import com.konsol.divinesign.domain.Splend;
import com.konsol.divinesign.domain.Verse;
import com.konsol.divinesign.service.api.dto.*;
import com.konsol.divinesign.service.dto.SplendDTO;
import java.util.List;
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
     * Get the "id" splend.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Splend> findOneDomainOwner(String id);

    /**
     * Get the "id" splend.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Splend> findOneDomainPublic(String id);

    /**
     * Delete the "id" splend.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     * API
     */

    /**
     * Create a new splend
     * @param createSplendPayload the payload to create a new splend
     * @return the created splend
     */
    SplendViewPayload createSplend(CreateSplend createSplendPayload);

    SplendVariablePayload createSplendVariable(String splendId, CreateSplendVariablePayload createSplendVariablePayload);

    List<SplendVariablePayload> getSplendVariablesByOwner(String splendId);

    SplendVariablePayload addVariableNameToVariable(
        String id,
        String variableId,
        com.konsol.divinesign.service.api.dto.VariablePayload variablePayload
    );

    SplendVariablePayload getSplendVariable(String id);

    void deleteSplendVariable(String id);

    void deleteSplendVariableNameFromVariable(String id, VariablePayload variablePayload);

    void likeSplend(String splendId);

    List<Comment> findCommentsBySplendId(String splendId);

    Comment createSplendComment(String splendId, CommentViewPayload commentViewPayload);

    List<Verse> getSplendVariableVerses(String id, String variableId);
}
