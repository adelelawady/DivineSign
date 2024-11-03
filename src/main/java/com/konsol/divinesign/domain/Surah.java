package com.konsol.divinesign.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Surah.
 */
@Document(collection = "surah")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Surah implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("surahId")
    private String surahId;

    @Field("name")
    private String name;

    @Field("transliteration")
    private String transliteration;

    @Field("type")
    private String type;

    @Field("total_verses")
    private Integer totalVerses;

    @DBRef
    @Field("verses")
    @JsonIgnoreProperties(value = { "surah", "splendVerses" }, allowSetters = true)
    private Set<Verse> verses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Surah id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Surah name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTransliteration() {
        return this.transliteration;
    }

    public Surah transliteration(String transliteration) {
        this.setTransliteration(transliteration);
        return this;
    }

    public void setTransliteration(String transliteration) {
        this.transliteration = transliteration;
    }

    public String getType() {
        return this.type;
    }

    public Surah type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTotalVerses() {
        return this.totalVerses;
    }

    public Surah totalVerses(Integer totalVerses) {
        this.setTotalVerses(totalVerses);
        return this;
    }

    public void setTotalVerses(Integer totalVerses) {
        this.totalVerses = totalVerses;
    }

    public Set<Verse> getVerses() {
        return this.verses;
    }

    public void setVerses(Set<Verse> verses) {
        if (this.verses != null) {
            this.verses.forEach(i -> i.setSurah(null));
        }
        if (verses != null) {
            verses.forEach(i -> i.setSurah(this));
        }
        this.verses = verses;
    }

    public Surah verses(Set<Verse> verses) {
        this.setVerses(verses);
        return this;
    }

    public Surah addVerses(Verse verse) {
        this.verses.add(verse);
        verse.setSurah(this);
        return this;
    }

    public Surah removeVerses(Verse verse) {
        this.verses.remove(verse);
        verse.setSurah(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Surah)) {
            return false;
        }
        return getId() != null && getId().equals(((Surah) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Surah{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", transliteration='" + getTransliteration() + "'" +
            ", type='" + getType() + "'" +
            ", totalVerses=" + getTotalVerses() +
            "}";
    }

    public String getSurahId() {
        return surahId;
    }

    public void setSurahId(String surahId) {
        this.surahId = surahId;
    }
}
