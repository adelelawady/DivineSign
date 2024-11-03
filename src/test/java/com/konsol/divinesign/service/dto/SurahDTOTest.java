package com.konsol.divinesign.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.divinesign.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SurahDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SurahDTO.class);
        SurahDTO surahDTO1 = new SurahDTO();
        surahDTO1.setId("id1");
        SurahDTO surahDTO2 = new SurahDTO();
        assertThat(surahDTO1).isNotEqualTo(surahDTO2);
        surahDTO2.setId(surahDTO1.getId());
        assertThat(surahDTO1).isEqualTo(surahDTO2);
        surahDTO2.setId("id2");
        assertThat(surahDTO1).isNotEqualTo(surahDTO2);
        surahDTO1.setId(null);
        assertThat(surahDTO1).isNotEqualTo(surahDTO2);
    }
}
