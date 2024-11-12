package com.konsol.divinesign.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.konsol.divinesign.service.api.dto.VariablePayload;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A SplendVerses.
 */
@Document(collection = "splend_variables")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SplendVariables implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("word")
    private String word;

    @Field("word_verse_count")
    private String wordVerseCount;

    @Field("regexCount")
    private String regexCount;

    @Field("regex")
    private String regex;

    @Field("variables")
    private Set<VariablePayload> variables = new LinkedHashSet<>();

    @DBRef
    @Field("splend")
    @JsonIgnoreProperties(value = { "user", "category", "verses", "tags", "likedUsers", "dislikedSplends" }, allowSetters = true)
    private Splend splend;

    @DBRef
    @Field("verses")
    @JsonIgnoreProperties(value = { "surah", "splendVerses" }, allowSetters = true)
    private Set<Verse> verses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public SplendVariables id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return this.word;
    }

    public SplendVariables word(String word) {
        this.setWord(word);
        return this;
    }

    public void setWord(String word) {
        this.word = word;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SplendVariables)) {
            return false;
        }
        return getId() != null && getId().equals(((SplendVariables) o).getId());
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
            "}";
    }

    public Set<Verse> getVerses() {
        return verses;
    }

    public void setVerses(Set<Verse> verses) {
        this.verses = verses;
    }

    public Splend getSplend() {
        return splend;
    }

    public void setSplend(Splend splend) {
        this.splend = splend;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getRegexCount() {
        return regexCount;
    }

    public void setRegexCount(String regexCount) {
        this.regexCount = regexCount;
    }

    public String getWordVerseCount() {
        return wordVerseCount;
    }

    public void setWordVerseCount(String wordVerseCount) {
        this.wordVerseCount = wordVerseCount;
    }

    public Set<VariablePayload> getVariables() {
        return variables;
    }

    public void setVariables(Set<VariablePayload> variables) {
        this.variables = variables;
    }
}
