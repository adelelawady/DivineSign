package com.konsol.divinesign.web.rest;

import com.konsol.divinesign.repository.VerseRepository;
import com.konsol.divinesign.service.VerseService;
import com.konsol.divinesign.service.dto.VerseDTO;
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
 * REST controller for managing {@link com.konsol.divinesign.domain.Verse}.
 */
@RestController
@RequestMapping("/api/verses")
public class VerseResource {

    private static final Logger LOG = LoggerFactory.getLogger(VerseResource.class);

    private static final String ENTITY_NAME = "verse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VerseService verseService;

    private final VerseRepository verseRepository;

    public VerseResource(VerseService verseService, VerseRepository verseRepository) {
        this.verseService = verseService;
        this.verseRepository = verseRepository;
    }

    /**
     * {@code POST  /verses} : Create a new verse.
     *
     * @param verseDTO the verseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new verseDTO, or with status {@code 400 (Bad Request)} if the verse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VerseDTO> createVerse(@RequestBody VerseDTO verseDTO) throws URISyntaxException {
        LOG.debug("REST request to save Verse : {}", verseDTO);
        if (verseDTO.getId() != null) {
            throw new BadRequestAlertException("A new verse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        verseDTO = verseService.save(verseDTO);
        return ResponseEntity.created(new URI("/api/verses/" + verseDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, verseDTO.getId()))
            .body(verseDTO);
    }

    /**
     * {@code PUT  /verses/:id} : Updates an existing verse.
     *
     * @param id the id of the verseDTO to save.
     * @param verseDTO the verseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated verseDTO,
     * or with status {@code 400 (Bad Request)} if the verseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the verseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VerseDTO> updateVerse(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody VerseDTO verseDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Verse : {}, {}", id, verseDTO);
        if (verseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, verseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!verseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        verseDTO = verseService.update(verseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, verseDTO.getId()))
            .body(verseDTO);
    }

    /**
     * {@code PATCH  /verses/:id} : Partial updates given fields of an existing verse, field will ignore if it is null
     *
     * @param id the id of the verseDTO to save.
     * @param verseDTO the verseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated verseDTO,
     * or with status {@code 400 (Bad Request)} if the verseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the verseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the verseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VerseDTO> partialUpdateVerse(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody VerseDTO verseDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Verse partially : {}, {}", id, verseDTO);
        if (verseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, verseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!verseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VerseDTO> result = verseService.partialUpdate(verseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, verseDTO.getId())
        );
    }

    /**
     * {@code GET  /verses} : get all the verses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of verses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VerseDTO>> getAllVerses(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Verses");
        Page<VerseDTO> page = verseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /verses/:id} : get the "id" verse.
     *
     * @param id the id of the verseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the verseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VerseDTO> getVerse(@PathVariable("id") String id) {
        LOG.debug("REST request to get Verse : {}", id);
        Optional<VerseDTO> verseDTO = verseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(verseDTO);
    }

    /**
     * {@code DELETE  /verses/:id} : delete the "id" verse.
     *
     * @param id the id of the verseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVerse(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Verse : {}", id);
        verseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
