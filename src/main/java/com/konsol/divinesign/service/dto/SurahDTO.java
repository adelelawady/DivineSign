package com.konsol.divinesign.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.konsol.divinesign.domain.Surah} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SurahDTO implements Serializable {

    private String id;

    private String name;

    private String transliteration;

    private String type;

    private Integer totalVerses;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTransliteration() {
        return transliteration;
    }

    public void setTransliteration(String transliteration) {
        this.transliteration = transliteration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTotalVerses() {
        return totalVerses;
    }

    public void setTotalVerses(Integer totalVerses) {
        this.totalVerses = totalVerses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SurahDTO)) {
            return false;
        }

        SurahDTO surahDTO = (SurahDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, surahDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SurahDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", transliteration='" + getTransliteration() + "'" +
            ", type='" + getType() + "'" +
            ", totalVerses=" + getTotalVerses() +
            "}";
    }
}
