package com.konsol.divinesign.web.rest;

import com.konsol.divinesign.repository.SplendRepository;
import com.konsol.divinesign.service.SplendService;
import com.konsol.divinesign.service.dto.SplendDTO;
import com.konsol.divinesign.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.konsol.divinesign.domain.Splend}.
 */
@RestController
@RequestMapping("/api/splends")
public class SplendResource {

    private static final Logger LOG = LoggerFactory.getLogger(SplendResource.class);

    private static final String ENTITY_NAME = "splend";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SplendService splendService;

    private final SplendRepository splendRepository;

    public SplendResource(SplendService splendService, SplendRepository splendRepository) {
        this.splendService = splendService;
        this.splendRepository = splendRepository;
    }

    /**
     * {@code POST  /splends} : Create a new splend.
     *
     * @param splendDTO the splendDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new splendDTO, or with status {@code 400 (Bad Request)} if the splend has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SplendDTO> createSplend(@Valid @RequestBody SplendDTO splendDTO) throws URISyntaxException {
        LOG.debug("REST request to save Splend : {}", splendDTO);
        if (splendDTO.getId() != null) {
            throw new BadRequestAlertException("A new splend cannot already have an ID", ENTITY_NAME, "idexists");
        }
        splendDTO = splendService.save(splendDTO);
        return ResponseEntity.created(new URI("/api/splends/" + splendDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, splendDTO.getId()))
            .body(splendDTO);
    }

    /**
     * {@code PUT  /splends/:id} : Updates an existing splend.
     *
     * @param id the id of the splendDTO to save.
     * @param splendDTO the splendDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated splendDTO,
     * or with status {@code 400 (Bad Request)} if the splendDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the splendDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SplendDTO> updateSplend(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody SplendDTO splendDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Splend : {}, {}", id, splendDTO);
        if (splendDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, splendDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!splendRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        splendDTO = splendService.update(splendDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, splendDTO.getId()))
            .body(splendDTO);
    }

    /**
     * {@code PATCH  /splends/:id} : Partial updates given fields of an existing splend, field will ignore if it is null
     *
     * @param id the id of the splendDTO to save.
     * @param splendDTO the splendDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated splendDTO,
     * or with status {@code 400 (Bad Request)} if the splendDTO is not valid,
     * or with status {@code 404 (Not Found)} if the splendDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the splendDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SplendDTO> partialUpdateSplend(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody SplendDTO splendDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Splend partially : {}, {}", id, splendDTO);
        if (splendDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, splendDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!splendRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SplendDTO> result = splendService.partialUpdate(splendDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, splendDTO.getId())
        );
    }

    /**
     * {@code GET  /splends} : get all the splends.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of splends in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SplendDTO>> getAllSplends(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Splends");
        Page<SplendDTO> page;
        if (eagerload) {
            page = splendService.findAllWithEagerRelationships(pageable);
        } else {
            page = splendService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /splends/:id} : get the "id" splend.
     *
     * @param id the id of the splendDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the splendDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SplendDTO> getSplend(@PathVariable("id") String id) {
        LOG.debug("REST request to get Splend : {}", id);
        Optional<SplendDTO> splendDTO = splendService.findOne(id);
        return ResponseUtil.wrapOrNotFound(splendDTO);
    }

    /**
     * {@code DELETE  /splends/:id} : delete the "id" splend.
     *
     * @param id the id of the splendDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSplend(@PathVariable("id") String id) {
        LOG.debug("REST request to delete Splend : {}", id);
        splendService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
