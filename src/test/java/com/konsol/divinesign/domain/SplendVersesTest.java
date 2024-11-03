package com.konsol.divinesign.domain;

import static com.konsol.divinesign.domain.SplendVersesTestSamples.*;
import static com.konsol.divinesign.domain.ValidationModelTestSamples.*;
import static com.konsol.divinesign.domain.VerseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.divinesign.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SplendVersesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SplendVerses.class);
        SplendVerses splendVerses1 = getSplendVersesSample1();
        SplendVerses splendVerses2 = new SplendVerses();
        assertThat(splendVerses1).isNotEqualTo(splendVerses2);

        splendVerses2.setId(splendVerses1.getId());
        assertThat(splendVerses1).isEqualTo(splendVerses2);

        splendVerses2 = getSplendVersesSample2();
        assertThat(splendVerses1).isNotEqualTo(splendVerses2);
    }

    @Test
    void validationMethodTest() {
        SplendVerses splendVerses = getSplendVersesRandomSampleGenerator();
        ValidationModel validationModelBack = getValidationModelRandomSampleGenerator();

        splendVerses.setValidationMethod(validationModelBack);
        assertThat(splendVerses.getValidationMethod()).isEqualTo(validationModelBack);

        splendVerses.validationMethod(null);
        assertThat(splendVerses.getValidationMethod()).isNull();
    }

    @Test
    void versesTest() {
        SplendVerses splendVerses = getSplendVersesRandomSampleGenerator();
        Verse verseBack = getVerseRandomSampleGenerator();

        splendVerses.addVerses(verseBack);
        assertThat(splendVerses.getVerses()).containsOnly(verseBack);
        assertThat(verseBack.getSplendVerses()).isEqualTo(splendVerses);

        splendVerses.removeVerses(verseBack);
        assertThat(splendVerses.getVerses()).doesNotContain(verseBack);
        assertThat(verseBack.getSplendVerses()).isNull();

        splendVerses.verses(new HashSet<>(Set.of(verseBack)));
        assertThat(splendVerses.getVerses()).containsOnly(verseBack);
        assertThat(verseBack.getSplendVerses()).isEqualTo(splendVerses);

        splendVerses.setVerses(new HashSet<>());
        assertThat(splendVerses.getVerses()).doesNotContain(verseBack);
        assertThat(verseBack.getSplendVerses()).isNull();
    }
}
