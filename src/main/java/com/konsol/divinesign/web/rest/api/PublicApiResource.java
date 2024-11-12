package com.konsol.divinesign.web.rest.api;

import com.konsol.divinesign.domain.Verse;
import com.konsol.divinesign.repository.SplendRepository;
import com.konsol.divinesign.repository.TestRecRepository;
import com.konsol.divinesign.repository.VerseRepository;
import com.konsol.divinesign.service.SplendService;
import com.konsol.divinesign.service.VerseService;
import com.konsol.divinesign.service.api.dto.SplendViewPayload;
import com.konsol.divinesign.service.api.dto.VerseSearchPayload;
import com.konsol.divinesign.service.api.dto.VerseSearchResultPayload;
import com.konsol.divinesign.service.mapper.api.SplendPayloadMapper;
import com.konsol.divinesign.service.mapper.api.VersePayloadMapper;
import com.konsol.divinesign.web.api.PublicApi;
import com.konsol.divinesign.web.api.PublicApiDelegate;
import com.konsol.divinesign.web.api.VerseApiDelegate;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * REST controller for managing {@link com.konsol.divinesign.domain.Splend}.
 */
@Service
public class PublicApiResource implements PublicApiDelegate {

    private static final Logger LOG = LoggerFactory.getLogger(PublicApiResource.class);

    private static final String ENTITY_NAME = "public";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SplendService splendService;

    private final SplendRepository splendRepository;

    private final VerseRepository verseRepository;

    private final TestRecRepository testRecRepository;

    private final SplendPayloadMapper splendPayloadMapper;

    private final VerseService verseService;

    private final VersePayloadMapper versePayloadMapper;

    public PublicApiResource(
        SplendService splendService,
        SplendRepository splendRepository,
        VerseRepository verseRepository,
        TestRecRepository testRecRepository,
        SplendPayloadMapper splendPayloadMapper,
        VerseService verseService,
        VersePayloadMapper versePayloadMapper
    ) {
        this.splendService = splendService;
        this.splendRepository = splendRepository;
        this.verseRepository = verseRepository;
        this.testRecRepository = testRecRepository;
        this.splendPayloadMapper = splendPayloadMapper;
        this.verseService = verseService;
        this.versePayloadMapper = versePayloadMapper;
    }

    @Override
    public ResponseEntity<List<SplendViewPayload>> getPublicSplends(Integer page, Integer size) {
        LOG.debug("REST request to get public Splends");
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(splendRepository.findAll(pageable).stream().map(splendPayloadMapper::toDto).collect(Collectors.toList()));
    }
}
