package com.konsol.divinesign.service.mapper;

import static com.konsol.divinesign.domain.SplendVersesAsserts.*;
import static com.konsol.divinesign.domain.SplendVersesTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SplendVersesMapperTest {

    private SplendVersesMapper splendVersesMapper;

    @BeforeEach
    void setUp() {
        splendVersesMapper = new SplendVersesMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSplendVersesSample1();
        var actual = splendVersesMapper.toEntity(splendVersesMapper.toDto(expected));
        assertSplendVersesAllPropertiesEquals(expected, actual);
    }
}
