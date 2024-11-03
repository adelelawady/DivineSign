package com.konsol.divinesign.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.divinesign.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ValidationModelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ValidationModelDTO.class);
        ValidationModelDTO validationModelDTO1 = new ValidationModelDTO();
        validationModelDTO1.setId("id1");
        ValidationModelDTO validationModelDTO2 = new ValidationModelDTO();
        assertThat(validationModelDTO1).isNotEqualTo(validationModelDTO2);
        validationModelDTO2.setId(validationModelDTO1.getId());
        assertThat(validationModelDTO1).isEqualTo(validationModelDTO2);
        validationModelDTO2.setId("id2");
        assertThat(validationModelDTO1).isNotEqualTo(validationModelDTO2);
        validationModelDTO1.setId(null);
        assertThat(validationModelDTO1).isNotEqualTo(validationModelDTO2);
    }
}
