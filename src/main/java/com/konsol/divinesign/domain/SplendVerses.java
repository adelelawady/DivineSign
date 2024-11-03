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
 * A SplendVerses.
 */
@Document(collection = "splend_verses")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SplendVerses implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("word")
    private String word;

    @Field("number")
    private String number;

    @Field("condition")
    private String condition;

    @DBRef
    @Field("validationMethod")
    private ValidationModel validationMethod;

    @DBRef
    @Field("verses")
    @JsonIgnoreProperties(value = { "surah", "splendVerses" }, allowSetters = true)
    private Set<Verse> verses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public SplendVerses id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return this.word;
    }

    public SplendVerses word(String word) {
        this.setWord(word);
        return this;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getNumber() {
        return this.number;
    }

    public SplendVerses number(String number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCondition() {
        return this.condition;
    }

    public SplendVerses condition(String condition) {
        this.setCondition(condition);
        return this;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public ValidationModel getValidationMethod() {
        return this.validationMethod;
    }

    public void setValidationMethod(ValidationModel validationModel) {
        this.validationMethod = validationModel;
    }

    public SplendVerses validationMethod(ValidationModel validationModel) {
        this.setValidationMethod(validationModel);
        return this;
    }

    public Set<Verse> getVerses() {
        return this.verses;
    }

    public void setVerses(Set<Verse> verses) {
        if (this.verses != null) {
            this.verses.forEach(i -> i.setSplendVerses(null));
        }
        if (verses != null) {
            verses.forEach(i -> i.setSplendVerses(this));
        }
        this.verses = verses;
    }

    public SplendVerses verses(Set<Verse> verses) {
        this.setVerses(verses);
        return this;
    }

    public SplendVerses addVerses(Verse verse) {
        this.verses.add(verse);
        verse.setSplendVerses(this);
        return this;
    }

    public SplendVerses removeVerses(Verse verse) {
        this.verses.remove(verse);
        verse.setSplendVerses(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SplendVerses)) {
            return false;
        }
        return getId() != null && getId().equals(((SplendVerses) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SplendVerses{" +
            "id=" + getId() +
            ", word='" + getWord() + "'" +
            ", number='" + getNumber() + "'" +
            ", condition='" + getCondition() + "'" +
            "}";
    }
}
