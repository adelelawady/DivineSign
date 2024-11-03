package com.konsol.divinesign.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.konsol.divinesign.domain.SplendVerses} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SplendVersesDTO implements Serializable {

    private String id;

    private String word;

    private String number;

    private String condition;

    private ValidationModelDTO validationMethod;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public ValidationModelDTO getValidationMethod() {
        return validationMethod;
    }

    public void setValidationMethod(ValidationModelDTO validationMethod) {
        this.validationMethod = validationMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SplendVersesDTO)) {
            return false;
        }

        SplendVersesDTO splendVersesDTO = (SplendVersesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, splendVersesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SplendVersesDTO{" +
            "id='" + getId() + "'" +
            ", word='" + getWord() + "'" +
            ", number='" + getNumber() + "'" +
            ", condition='" + getCondition() + "'" +
            ", validationMethod=" + getValidationMethod() +
            "}";
    }
}
