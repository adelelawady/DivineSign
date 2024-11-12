package com.konsol.divinesign.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Verse.
 */
@Document(collection = "testRecord")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("word")
    private String word;

    @Field("wordCount")
    private BigDecimal wordCount;

    @Field("versesCount")
    private BigDecimal versesCount;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public BigDecimal getWordCount() {
        return wordCount;
    }

    public void setWordCount(BigDecimal wordCount) {
        this.wordCount = wordCount;
    }

    public BigDecimal getVersesCount() {
        return versesCount;
    }

    public void setVersesCount(BigDecimal versesCount) {
        this.versesCount = versesCount;
    }
}
