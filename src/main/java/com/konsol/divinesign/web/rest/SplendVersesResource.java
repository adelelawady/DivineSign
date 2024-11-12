package com.konsol.divinesign.web.rest;

import com.konsol.divinesign.domain.SplendVariables;
import com.konsol.divinesign.repository.SplendVariablesRepository;
import com.konsol.divinesign.service.SplendVariablesService;
import com.konsol.divinesign.service.dto.SplendVersesDTO;
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
 * REST controller for managing {@link SplendVariables}.
 */
@RestController
@RequestMapping("/api/splend-verses")
public class SplendVersesResource {

    private static final Logger LOG = LoggerFactory.getLogger(SplendVersesResource.class);

    private static final String ENTITY_NAME = "splendVerses";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SplendVariablesService splendVariablesService;

    private final SplendVariablesRepository splendVariablesRepository;

    public SplendVersesResource(SplendVariablesService splendVariablesService, SplendVariablesRepository splendVariablesRepository) {
        this.splendVariablesService = splendVariablesService;
        this.splendVariablesRepository = splendVariablesRepository;
    }

    /**
     * {@code POST  /splend-verses} : Create a new splendVerses.
     *
     * @param splendVersesDTO the splendVersesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new splendVersesDTO, or with status {@code 400 (Bad Request)} if the splendVerses has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SplendVersesDTO> createSplendVerses(@RequestBody SplendVersesDTO splendVersesDTO) throws URISyntaxException {
        LOG.debug("REST request to save SplendVerses : {}", splendVersesDTO);
        if (splendVersesDTO.getId() != null) {
            throw new BadRequestAlertException("A new splendVerses cannot already have an ID", ENTITY_NAME, "idexists");
        }
        splendVersesDTO = splendVariablesService.save(splendVersesDTO);
        return ResponseEntity.created(new URI("/api/splend-verses/" + splendVersesDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, splendVersesDTO.getId()))
            .body(splendVersesDTO);
    }

    /**
     * {@code PUT  /splend-verses/:id} : Updates an existing splendVerses.
     *
     * @param id the id of the splendVersesDTO to save.
     * @param splendVersesDTO the splendVersesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated splendVersesDTO,
     * or with status {@code 400 (Bad Request)} if the splendVersesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the splendVersesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SplendVersesDTO> updateSplendVerses(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody SplendVersesDTO splendVersesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SplendVerses : {}, {}", id, splendVersesDTO);
        if (splendVersesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, splendVersesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!splendVariablesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        splendVersesDTO = splendVariablesService.update(splendVersesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, splendVersesDTO.getId()))
            .body(splendVersesDTO);
    }

    /**
     * {@code PATCH  /splend-verses/:id} : Partial updates given fields of an existing splendVerses, field will ignore if it is null
     *
     * @param id the id of the splendVersesDTO to save.
     * @param splendVersesDTO the splendVersesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated splendVersesDTO,
     * or with status {@code 400 (Bad Request)} if the splendVersesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the splendVersesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the splendVersesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SplendVersesDTO> partialUpdateSplendVerses(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody SplendVersesDTO splendVersesDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SplendVerses partially : {}, {}", id, splendVersesDTO);
        if (splendVersesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, splendVersesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!splendVariablesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SplendVersesDTO> result = splendVariablesService.partialUpdate(splendVersesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, splendVersesDTO.getId())
        );
    }

    /**
     * {@code GET  /splend-verses} : get all the splendVerses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of splendVerses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SplendVersesDTO>> getAllSplendVerses(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of SplendVerses");
        Page<SplendVersesDTO> page = splendVariablesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /splend-verses/:id} : get the "id" splendVerses.
     *
     * @param id the id of the splendVersesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the splendVersesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SplendVersesDTO> getSplendVerses(@PathVariable("id") String id) {
        LOG.debug("REST request to get SplendVerses : {}", id);
        Optional<SplendVersesDTO> splendVersesDTO = splendVariablesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(splendVersesDTO);
    }

    /**
     * {@code DELETE  /splend-verses/:id} : delete the "id" splendVerses.
     *
     * @param id the id of the splendVersesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSplendVerses(@PathVariable("id") String id) {
        LOG.debug("REST request to delete SplendVerses : {}", id);
        splendVariablesService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
