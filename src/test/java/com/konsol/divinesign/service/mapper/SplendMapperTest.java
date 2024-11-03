package com.konsol.divinesign.service.mapper;

import static com.konsol.divinesign.domain.SplendAsserts.*;
import static com.konsol.divinesign.domain.SplendTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SplendMapperTest {

    private SplendMapper splendMapper;

    @BeforeEach
    void setUp() {
        splendMapper = new SplendMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSplendSample1();
        var actual = splendMapper.toEntity(splendMapper.toDto(expected));
        assertSplendAllPropertiesEquals(expected, actual);
    }
}
