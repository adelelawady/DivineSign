package com.konsol.divinesign.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.divinesign.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SplendVariablesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SplendVersesDTO.class);
        SplendVersesDTO splendVersesDTO1 = new SplendVersesDTO();
        splendVersesDTO1.setId("id1");
        SplendVersesDTO splendVersesDTO2 = new SplendVersesDTO();
        assertThat(splendVersesDTO1).isNotEqualTo(splendVersesDTO2);
        splendVersesDTO2.setId(splendVersesDTO1.getId());
        assertThat(splendVersesDTO1).isEqualTo(splendVersesDTO2);
        splendVersesDTO2.setId("id2");
        assertThat(splendVersesDTO1).isNotEqualTo(splendVersesDTO2);
        splendVersesDTO1.setId(null);
        assertThat(splendVersesDTO1).isNotEqualTo(splendVersesDTO2);
    }
}
