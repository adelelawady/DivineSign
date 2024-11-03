package com.konsol.divinesign.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.divinesign.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SplendDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SplendDTO.class);
        SplendDTO splendDTO1 = new SplendDTO();
        splendDTO1.setId("id1");
        SplendDTO splendDTO2 = new SplendDTO();
        assertThat(splendDTO1).isNotEqualTo(splendDTO2);
        splendDTO2.setId(splendDTO1.getId());
        assertThat(splendDTO1).isEqualTo(splendDTO2);
        splendDTO2.setId("id2");
        assertThat(splendDTO1).isNotEqualTo(splendDTO2);
        splendDTO1.setId(null);
        assertThat(splendDTO1).isNotEqualTo(splendDTO2);
    }
}
