package com.konsol.divinesign.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.konsol.divinesign.domain.Verse} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VerseDTO implements Serializable {

    private String id;

    private String verse;

    private String diacriticVerse;

    private SurahDTO surah;

    private SplendVersesDTO splendVerses;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVerse() {
        return verse;
    }

    public void setVerse(String verse) {
        this.verse = verse;
    }

    public String getDiacriticVerse() {
        return diacriticVerse;
    }

    public void setDiacriticVerse(String diacriticVerse) {
        this.diacriticVerse = diacriticVerse;
    }

    public SurahDTO getSurah() {
        return surah;
    }

    public void setSurah(SurahDTO surah) {
        this.surah = surah;
    }

    public SplendVersesDTO getSplendVerses() {
        return splendVerses;
    }

    public void setSplendVerses(SplendVersesDTO splendVerses) {
        this.splendVerses = splendVerses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VerseDTO)) {
            return false;
        }

        VerseDTO verseDTO = (VerseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, verseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VerseDTO{" +
            "id='" + getId() + "'" +
            ", verse='" + getVerse() + "'" +
            ", diacriticVerse='" + getDiacriticVerse() + "'" +
            ", surah=" + getSurah() +
            ", splendVerses=" + getSplendVerses() +
            "}";
    }
}
