package com.konsol.divinesign.domain;

import static com.konsol.divinesign.domain.SplendVersesTestSamples.*;
import static com.konsol.divinesign.domain.SurahTestSamples.*;
import static com.konsol.divinesign.domain.VerseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.divinesign.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VerseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Verse.class);
        Verse verse1 = getVerseSample1();
        Verse verse2 = new Verse();
        assertThat(verse1).isNotEqualTo(verse2);

        verse2.setId(verse1.getId());
        assertThat(verse1).isEqualTo(verse2);

        verse2 = getVerseSample2();
        assertThat(verse1).isNotEqualTo(verse2);
    }

    @Test
    void surahTest() {
        Verse verse = getVerseRandomSampleGenerator();
        Surah surahBack = getSurahRandomSampleGenerator();

        verse.setSurah(surahBack);
        assertThat(verse.getSurah()).isEqualTo(surahBack);

        verse.surah(null);
        assertThat(verse.getSurah()).isNull();
    }

    @Test
    void splendVersesTest() {
        Verse verse = getVerseRandomSampleGenerator();
        SplendVerses splendVersesBack = getSplendVersesRandomSampleGenerator();

        verse.setSplendVerses(splendVersesBack);
        assertThat(verse.getSplendVerses()).isEqualTo(splendVersesBack);

        verse.splendVerses(null);
        assertThat(verse.getSplendVerses()).isNull();
    }
}
