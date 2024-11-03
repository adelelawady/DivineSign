package com.konsol.divinesign.service.mapper;

import static com.konsol.divinesign.domain.SurahAsserts.*;
import static com.konsol.divinesign.domain.SurahTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SurahMapperTest {

    private SurahMapper surahMapper;

    @BeforeEach
    void setUp() {
        surahMapper = new SurahMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSurahSample1();
        var actual = surahMapper.toEntity(surahMapper.toDto(expected));
        assertSurahAllPropertiesEquals(expected, actual);
    }
}
