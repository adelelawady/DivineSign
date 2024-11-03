package com.konsol.divinesign.domain;

import static com.konsol.divinesign.domain.ValidationModelTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.divinesign.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ValidationModelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ValidationModel.class);
        ValidationModel validationModel1 = getValidationModelSample1();
        ValidationModel validationModel2 = new ValidationModel();
        assertThat(validationModel1).isNotEqualTo(validationModel2);

        validationModel2.setId(validationModel1.getId());
        assertThat(validationModel1).isEqualTo(validationModel2);

        validationModel2 = getValidationModelSample2();
        assertThat(validationModel1).isNotEqualTo(validationModel2);
    }
}
