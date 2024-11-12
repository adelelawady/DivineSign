package com.konsol.divinesign.web.rest.api;

import com.konsol.divinesign.domain.TestRecord;
import com.konsol.divinesign.domain.Verse;
import com.konsol.divinesign.repository.SplendRepository;
import com.konsol.divinesign.repository.TestRecRepository;
import com.konsol.divinesign.repository.VerseRepository;
import com.konsol.divinesign.service.SplendService;
import com.konsol.divinesign.service.VerseService;
import com.konsol.divinesign.service.api.dto.CreateSplend;
import com.konsol.divinesign.service.api.dto.SplendViewPayload;
import com.konsol.divinesign.service.api.dto.VerseSearchPayload;
import com.konsol.divinesign.service.api.dto.VerseSearchResultPayload;
import com.konsol.divinesign.service.mapper.api.VersePayloadMapper;
import com.konsol.divinesign.web.api.SplendApiDelegate;
import com.konsol.divinesign.web.api.VerseApiDelegate;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * REST controller for managing {@link com.konsol.divinesign.domain.Splend}.
 */
@Service
public class VerseApiResource implements VerseApiDelegate {

    private static final Logger LOG = LoggerFactory.getLogger(VerseApiResource.class);

    private static final String ENTITY_NAME = "splend";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SplendService splendService;

    private final SplendRepository splendRepository;

    private final VerseRepository verseRepository;

    private final TestRecRepository testRecRepository;

    private final VerseService verseService;

    private final VersePayloadMapper versePayloadMapper;

    public VerseApiResource(
        SplendService splendService,
        SplendRepository splendRepository,
        VerseRepository verseRepository,
        TestRecRepository testRecRepository,
        VerseService verseService,
        VersePayloadMapper versePayloadMapper
    ) {
        this.splendService = splendService;
        this.splendRepository = splendRepository;
        this.verseRepository = verseRepository;
        this.testRecRepository = testRecRepository;
        this.verseService = verseService;
        this.versePayloadMapper = versePayloadMapper;
    }

    @Override
    public ResponseEntity<VerseSearchResultPayload> searchVerse(VerseSearchPayload verseSearchPayload) {
        VerseSearchResultPayload verseSearchResultPayload = new VerseSearchResultPayload();

        List<Verse> foundVerses = verseService.searchVersesByWord(verseSearchPayload.getWordQuery());
        verseSearchResultPayload.setVersesCount(BigDecimal.valueOf(foundVerses.size()));
        verseSearchResultPayload.setVerses(foundVerses.stream().map(versePayloadMapper::toDto).toList());
        verseSearchResultPayload.setWordCount(
            BigDecimal.valueOf(verseService.findWordOccurrencesInVerseList(foundVerses, verseSearchPayload.getRegexQuery()))
        );

        /* List<Verse> verseList = verseRepository.findAll();

        for(Verse verse:verseList){

            for (String word :verse.getDiacriticVerse().split(" ")){

                List<TestRecord> testRecordList=testRecRepository.findAllByWord(word);

                if (testRecordList.isEmpty()){

                    // new word

                    TestRecord testRecord= new TestRecord();
                    testRecord.setWord(word);
                    testRecord=  testRecRepository.save(testRecord);


                    VerseSearchResultPayload verseSearchResultPayloadX= new VerseSearchResultPayload();
                    List<Verse> foundVersesX=verseService.searchVersesByWord(word);
                    verseSearchResultPayloadX.setVersesCount(BigDecimal.valueOf(foundVersesX.size()));
                    verseSearchResultPayloadX.setVerses(foundVersesX.stream().map(versePayloadMapper::toDto).toList());
                    verseSearchResultPayloadX.setWordCount(BigDecimal.valueOf(verseService.findWordOccurrencesInVerseList(foundVersesX,word)));


                    testRecord.setVersesCount(verseSearchResultPayloadX.getVersesCount());
                    testRecord.setWordCount(verseSearchResultPayloadX.getWordCount());
                    testRecRepository.save(testRecord);
                }else{
                    continue;
                }


            }

        }
*/

        return ResponseEntity.ok(verseSearchResultPayload);
    }
}
