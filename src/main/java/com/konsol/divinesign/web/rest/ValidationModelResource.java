package com.konsol.divinesign.web.rest;

import com.konsol.divinesign.repository.ValidationModelRepository;
import com.konsol.divinesign.service.ValidationModelService;
import com.konsol.divinesign.service.dto.ValidationModelDTO;
import com.konsol.divinesign.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.konsol.divinesign.domain.ValidationModel}.
 */
@RestController
@RequestMapping("/api/validation-models")
public class ValidationModelResource {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationModelResource.class);

    private static final String ENTITY_NAME = "validationModel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ValidationModelService validationModelService;

    private final ValidationModelRepository validationModelRepository;

    public ValidationModelResource(ValidationModelService validationModelService, ValidationModelRepository validationModelRepository) {
        this.validationModelService = validationModelService;
        this.validationModelRepository = validationModelRepository;
    }

    /**
     * {@code POST  /validation-models} : Create a new validationModel.
     *
     * @param validationModelDTO the validationModelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new validationModelDTO, or with status {@code 400 (Bad Request)} if the validationModel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ValidationModelDTO> createValidationModel(@RequestBody ValidationModelDTO validationModelDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ValidationModel : {}", validationModelDTO);
        if (validationModelDTO.getId() != null) {
            throw new BadRequestAlertException("A new validationModel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        validationModelDTO = validationModelService.save(validationModelDTO);
        return ResponseEntity.created(new URI("/api/validation-models/" + validationModelDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, validationModelDTO.getId()))
            .body(validationModelDTO);
    }

    /**
     * {@code PUT  /validation-models/:id} : Updates an existing validationModel.
     *
     * @param id the id of the validationModelDTO to save.
     * @param validationModelDTO the validationModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated validationModelDTO,
     * or with status {@code 400 (Bad Request)} if the validationModelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the validationModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ValidationModelDTO> updateValidationModel(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ValidationModelDTO validationModelDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ValidationModel : {}, {}", id, validationModelDTO);
        if (validationModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, validationModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!validationModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        validationModelDTO = validationModelService.update(validationModelDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, validationModelDTO.getId()))
            .body(validationModelDTO);
    }

    /**
     * {@code PATCH  /validation-models/:id} : Partial updates given fields of an existing validationModel, field will ignore if it is null
     *
     * @param id the id of the validationModelDTO to save.
     * @param validationModelDTO the validationModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated validationModelDTO,
     * or with status {@code 400 (Bad Request)} if the validationModelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the validationModelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the validationModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ValidationModelDTO> partialUpdateValidationModel(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ValidationModelDTO validationModelDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ValidationModel partially : {}, {}", id, validationModelDTO);
        if (validationModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, validationModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!validationModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ValidationModelDTO> result = validationModelService.partialUpdate(validationModelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, validationModelDTO.getId())
        );
    }

    /**
     * {@code GET  /validation-models} : get all the validationModels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of validationModels in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ValidationModelDTO>> getAllValidationModels(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ValidationModels");
        Page<ValidationModelDTO> page = validationModelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /validation-models/:id} : get the "id" validationModel.
     *
     * @param id the id of the validationModelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the validationModelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ValidationModelDTO> getValidationModel(@PathVariable("id") String id) {
        LOG.debug("REST request to get ValidationModel : {}", id);
        Optional<ValidationModelDTO> validationModelDTO = validationModelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(validationModelDTO);
    }

    /**
     * {@code DELETE  /validation-models/:id} : delete the "id" validationModel.
     *
     * @param id the id of the validationModelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteValidationModel(@PathVariable("id") String id) {
        LOG.debug("REST request to delete ValidationModel : {}", id);
        validationModelService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
