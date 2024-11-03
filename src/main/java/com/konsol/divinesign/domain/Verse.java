package com.konsol.divinesign.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Verse.
 */
@Document(collection = "verse")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Verse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("verseId")
    private String verseId;

    @Field("verse")
    private String verse;

    @Field("diacritic_verse")
    private String diacriticVerse;

    @DBRef
    @Field("surah")
    @JsonIgnoreProperties(value = { "verses" }, allowSetters = true)
    private Surah surah;

    @DBRef
    @Field("splendVerses")
    @JsonIgnoreProperties(value = { "validationMethod", "verses" }, allowSetters = true)
    private SplendVerses splendVerses;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Verse id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVerse() {
        return this.verse;
    }

    public Verse verse(String verse) {
        this.setVerse(verse);
        return this;
    }

    public void setVerse(String verse) {
        this.verse = verse;
    }

    public String getDiacriticVerse() {
        return this.diacriticVerse;
    }

    public Verse diacriticVerse(String diacriticVerse) {
        this.setDiacriticVerse(diacriticVerse);
        return this;
    }

    public void setDiacriticVerse(String diacriticVerse) {
        this.diacriticVerse = diacriticVerse;
    }

    public Surah getSurah() {
        return this.surah;
    }

    public void setSurah(Surah surah) {
        this.surah = surah;
    }

    public Verse surah(Surah surah) {
        this.setSurah(surah);
        return this;
    }

    public SplendVerses getSplendVerses() {
        return this.splendVerses;
    }

    public void setSplendVerses(SplendVerses splendVerses) {
        this.splendVerses = splendVerses;
    }

    public Verse splendVerses(SplendVerses splendVerses) {
        this.setSplendVerses(splendVerses);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Verse)) {
            return false;
        }
        return getId() != null && getId().equals(((Verse) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Verse{" +
            "id=" + getId() +
            ", verse='" + getVerse() + "'" +
            ", diacriticVerse='" + getDiacriticVerse() + "'" +
            "}";
    }

    public String getVerseId() {
        return verseId;
    }

    public void setVerseId(String verseId) {
        this.verseId = verseId;
    }
}
