package com.konsol.divinesign.service.mapper;

import static com.konsol.divinesign.domain.VerseAsserts.*;
import static com.konsol.divinesign.domain.VerseTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VerseMapperTest {

    private VerseMapper verseMapper;

    @BeforeEach
    void setUp() {
        verseMapper = new VerseMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVerseSample1();
        var actual = verseMapper.toEntity(verseMapper.toDto(expected));
        assertVerseAllPropertiesEquals(expected, actual);
    }
}
