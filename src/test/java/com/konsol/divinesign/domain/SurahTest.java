package com.konsol.divinesign.domain;

import static com.konsol.divinesign.domain.SurahTestSamples.*;
import static com.konsol.divinesign.domain.VerseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.divinesign.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SurahTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Surah.class);
        Surah surah1 = getSurahSample1();
        Surah surah2 = new Surah();
        assertThat(surah1).isNotEqualTo(surah2);

        surah2.setId(surah1.getId());
        assertThat(surah1).isEqualTo(surah2);

        surah2 = getSurahSample2();
        assertThat(surah1).isNotEqualTo(surah2);
    }

    @Test
    void versesTest() {
        Surah surah = getSurahRandomSampleGenerator();
        Verse verseBack = getVerseRandomSampleGenerator();

        surah.addVerses(verseBack);
        assertThat(surah.getVerses()).containsOnly(verseBack);
        assertThat(verseBack.getSurah()).isEqualTo(surah);

        surah.removeVerses(verseBack);
        assertThat(surah.getVerses()).doesNotContain(verseBack);
        assertThat(verseBack.getSurah()).isNull();

        surah.verses(new HashSet<>(Set.of(verseBack)));
        assertThat(surah.getVerses()).containsOnly(verseBack);
        assertThat(verseBack.getSurah()).isEqualTo(surah);

        surah.setVerses(new HashSet<>());
        assertThat(surah.getVerses()).doesNotContain(verseBack);
        assertThat(verseBack.getSurah()).isNull();
    }
}
