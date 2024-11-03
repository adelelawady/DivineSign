package com.konsol.divinesign.service.mapper;

import static com.konsol.divinesign.domain.ValidationModelAsserts.*;
import static com.konsol.divinesign.domain.ValidationModelTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ValidationModelMapperTest {

    private ValidationModelMapper validationModelMapper;

    @BeforeEach
    void setUp() {
        validationModelMapper = new ValidationModelMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getValidationModelSample1();
        var actual = validationModelMapper.toEntity(validationModelMapper.toDto(expected));
        assertValidationModelAllPropertiesEquals(expected, actual);
    }
}
