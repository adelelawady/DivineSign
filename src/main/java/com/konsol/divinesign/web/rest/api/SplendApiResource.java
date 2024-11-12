package com.konsol.divinesign.web.rest.api;

import com.konsol.divinesign.repository.SplendRepository;
import com.konsol.divinesign.service.SplendService;
import com.konsol.divinesign.service.api.dto.*;
import com.konsol.divinesign.service.mapper.api.CommentPayloadMapper;
import com.konsol.divinesign.service.mapper.api.SplendPayloadMapper;
import com.konsol.divinesign.service.mapper.api.VersePayloadMapper;
import com.konsol.divinesign.web.api.SplendApi;
import com.konsol.divinesign.web.api.SplendApiDelegate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

/**
 * REST controller for managing {@link com.konsol.divinesign.domain.Splend}.
 */
@Service
public class SplendApiResource implements SplendApiDelegate {

    private static final Logger LOG = LoggerFactory.getLogger(SplendApiResource.class);

    private static final String ENTITY_NAME = "splend";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SplendService splendService;

    private final SplendPayloadMapper splendPayloadMapper;

    private final SplendRepository splendRepository;

    private final VersePayloadMapper versePayloadMapper;

    private final CommentPayloadMapper commentPayloadMapper;

    public SplendApiResource(
        SplendService splendService,
        SplendPayloadMapper splendPayloadMapper,
        SplendRepository splendRepository,
        VersePayloadMapper versePayloadMapper,
        CommentPayloadMapper commentPayloadMapper
    ) {
        this.splendService = splendService;
        this.splendPayloadMapper = splendPayloadMapper;
        this.splendRepository = splendRepository;
        this.versePayloadMapper = versePayloadMapper;
        this.commentPayloadMapper = commentPayloadMapper;
    }

    @Override
    public ResponseEntity<SplendViewPayload> createSplend(CreateSplend createSplend) {
        LOG.debug("REST request to save Splend : {}", createSplend);
        return ResponseEntity.ok(splendService.createSplend(createSplend));
    }

    @Override
    public ResponseEntity<SplendViewPayload> getSplend(String id) {
        LOG.debug("REST request to get user owner Splend : {}", id);
        Optional<SplendViewPayload> splendViewPayload = splendRepository.findById(id).map(splendPayloadMapper::toDto);
        return splendViewPayload.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<SplendVariablePayload> createSplendVariable(String id, CreateSplendVariablePayload createSplendVariablePayload) {
        LOG.debug("REST request to save SplendVariable : {}", createSplendVariablePayload);
        return ResponseEntity.ok(splendService.createSplendVariable(id, createSplendVariablePayload));
    }

    @Override
    public ResponseEntity<List<SplendVariablePayload>> getSplendVariables(String id) {
        LOG.debug("REST request to get SplendVariables : {}", id);
        return ResponseEntity.ok(splendService.getSplendVariablesByOwner(id));
    }

    @Override
    public ResponseEntity<SplendVariablePayload> addVariableNameToVariable(String id, String variableId, VariablePayload variablePayload) {
        LOG.debug("REST request to add VariableNameToVariable : {}", variablePayload);
        return ResponseEntity.ok(splendService.addVariableNameToVariable(id, variableId, variablePayload));
    }

    @Override
    public ResponseEntity<SplendVariablePayload> getSplendVariable(String id, String variableId) {
        return ResponseEntity.ok(splendService.getSplendVariable(variableId));
    }

    @Override
    public ResponseEntity<SplendVariablePayload> deleteVariableName(String id, String variableId, String name) {
        VariablePayload variablePayload = new VariablePayload();
        variablePayload.setName(name);

        LOG.debug("REST request to delete VariableName : {}", variablePayload);
        splendService.deleteSplendVariableNameFromVariable(variableId, variablePayload);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteSplendVariable(String id, String id2) {
        LOG.debug("REST request to delete SplendVariable : {}", id2);
        splendService.deleteSplendVariable(id2);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<SplendViewPayload> likeSplend(String id) {
        splendService.likeSplend(id);
        return splendService
            .findOneDomainPublic(id)
            .map(splendPayloadMapper::toDto)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<CommentViewPayload>> getSplendComments(String id) {
        LOG.debug("REST request to get SplendComments : {}", id);
        return ResponseEntity.ok(
            splendService.findCommentsBySplendId(id).stream().map(commentPayloadMapper::toDto).collect(Collectors.toList())
        );
    }

    @Override
    public ResponseEntity<CommentViewPayload> createSplendComment(String id, CommentViewPayload commentViewPayload) {
        LOG.debug("REST request to save Comment : {}", commentViewPayload);
        return ResponseEntity.ok(commentPayloadMapper.toDto(splendService.createSplendComment(id, commentViewPayload)));
    }

    @Override
    public ResponseEntity<List<VersePayload>> getSplendVariableVerses(String id, String variableId) {
        LOG.debug("REST request to get SplendVariableVerses : {}", variableId);
        return ResponseEntity.ok(
            splendService.getSplendVariableVerses(id, variableId).stream().map(versePayloadMapper::toDto).collect(Collectors.toList())
        );
    }
}
