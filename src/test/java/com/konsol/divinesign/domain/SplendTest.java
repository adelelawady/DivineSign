package com.konsol.divinesign.domain;

import static com.konsol.divinesign.domain.CategoryTestSamples.*;
import static com.konsol.divinesign.domain.SplendTestSamples.*;
import static com.konsol.divinesign.domain.SplendVersesTestSamples.*;
import static com.konsol.divinesign.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.divinesign.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SplendTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Splend.class);
        Splend splend1 = getSplendSample1();
        Splend splend2 = new Splend();
        assertThat(splend1).isNotEqualTo(splend2);

        splend2.setId(splend1.getId());
        assertThat(splend1).isEqualTo(splend2);

        splend2 = getSplendSample2();
        assertThat(splend1).isNotEqualTo(splend2);
    }

    @Test
    void categoryTest() {
        Splend splend = getSplendRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        splend.setCategory(categoryBack);
        assertThat(splend.getCategory()).isEqualTo(categoryBack);

        splend.category(null);
        assertThat(splend.getCategory()).isNull();
    }

    @Test
    void versesTest() {
        Splend splend = getSplendRandomSampleGenerator();
        SplendVerses splendVersesBack = getSplendVersesRandomSampleGenerator();

        splend.setVerses(splendVersesBack);
        assertThat(splend.getVerses()).isEqualTo(splendVersesBack);

        splend.verses(null);
        assertThat(splend.getVerses()).isNull();
    }

    @Test
    void tagsTest() {
        Splend splend = getSplendRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        splend.addTags(tagBack);
        assertThat(splend.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getSplend()).isEqualTo(splend);

        splend.removeTags(tagBack);
        assertThat(splend.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getSplend()).isNull();

        splend.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(splend.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getSplend()).isEqualTo(splend);

        splend.setTags(new HashSet<>());
        assertThat(splend.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getSplend()).isNull();
    }
}
