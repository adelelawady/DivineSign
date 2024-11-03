package com.konsol.divinesign.domain;

import static com.konsol.divinesign.domain.RequestTestSamples.*;
import static com.konsol.divinesign.domain.SplendTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.divinesign.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Request.class);
        Request request1 = getRequestSample1();
        Request request2 = new Request();
        assertThat(request1).isNotEqualTo(request2);

        request2.setId(request1.getId());
        assertThat(request1).isEqualTo(request2);

        request2 = getRequestSample2();
        assertThat(request1).isNotEqualTo(request2);
    }

    @Test
    void splendTest() {
        Request request = getRequestRandomSampleGenerator();
        Splend splendBack = getSplendRandomSampleGenerator();

        request.setSplend(splendBack);
        assertThat(request.getSplend()).isEqualTo(splendBack);

        request.splend(null);
        assertThat(request.getSplend()).isNull();
    }
}
