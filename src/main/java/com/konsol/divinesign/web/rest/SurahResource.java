package com.konsol.divinesign.web.rest;

import com.konsol.divinesign.repository.SurahRepository;
import com.konsol.divinesign.service.SurahService;
import com.konsol.divinesign.service.dto.SurahDTO;
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
 * REST controller for managing {@link com.konsol.divinesign.domain.Surah}.
 */
@RestController
@RequestMapping("/api/surahs")
public class SurahResource {

    private static final Logger LOG = LoggerFactory.getLogger(SurahResource.class);

    private static final String ENTITY_NAME = "surah";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SurahService surahService;

    private final SurahRepository surahRepository;

    public SurahResource(SurahService surahService, SurahRepository surahRepository) {
        this.surahService = surahService;
        this.surahRepository = surahRepository;
    }

    /**
     * {@code POST  /surahs} : Create a new surah.
     *
     * @param surahDTO the surahDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new surahDTO, or with status {@code 400 (Bad Request)} if the surah has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SurahDTO> createSurah(@RequestBody SurahDTO surahDTO) throws URISyntaxException {
        LOG.debug("REST request to save Surah : {}", surahDTO);
        if (surahDTO.getId() != null) {
            throw new BadRequestAlertException("A new surah cannot already have an ID", ENTITY_NAME, "idexists");
        }
        surahDTO = surahService.save(surahDTO);
        return ResponseEntity.created(new URI("/api/surahs/" + surahDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, surahDTO.getId()))
            .body(surahDTO);
    }

    /**
     * {@code PUT  /surahs/:id} : Updates an existing surah.
     *
     * @param id the id of the surahDTO to save.
     * @param surahDTO the surahDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated surahDTO,
     * or with status {@code 400 (Bad Request)} if the surahDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the surahDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SurahDTO> updateSurah(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody SurahDTO surahDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Surah : {}, {}", id, surahDTO);
        if (surahDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, surahDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surahRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        surahDTO = surahService.update(surahDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, surahDTO.getId()))
            .body(surahDTO);
    }

    /**
     * {@code PATCH  /surahs/:id} : Partial updates given fields of an existing surah, field will ignore if it is null
     *
     * @param id the id of the surahDTO to save.
     * @param surahDTO the surahDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated surahDTO,
     * or with status {@code 400 (Bad Request)} if the surahDTO is not valid,
     * or with status {@code 404 (Not Found)} if the surahDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the surahDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SurahDTO> partialUpdateSurah(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody SurahDTO surahDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Surah partially : {}, {}", id, surahDTO);
        if (surahDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, surahDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!surahRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SurahDTO> result = surahService.partialUpdate(surahDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, surahDTO.getId())
        );
    }

    /**
     * {@code GET  /surahs} : get all the surahs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of surahs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SurahDTO>> getAllSurahs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Surahs");
        Page<SurahDTO> page = surahService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /surahs/:id} : get the "id" surah.
     *
     * @param id the id of the surahDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the surahDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SurahDTO> getSurah(@PathVariable("id") String id) {
        LOG.debug("REST request to get Surah : {}", id);
        Optional<SurahDTO> surahDTO = surahService.findOne(id);
        return ResponseUtil.wrapOrNotFound(surahDTO);
    }

    /**
     * {@code DELETE  /surahs/:id} : delete the "id" surah.
     *
     * @param id the id of the surahDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurah(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Surah : {}", id);
        surahService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
