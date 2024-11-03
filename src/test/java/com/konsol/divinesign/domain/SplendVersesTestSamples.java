package com.konsol.divinesign.domain;

import java.util.UUID;

public class SplendVersesTestSamples {

    public static SplendVerses getSplendVersesSample1() {
        return new SplendVerses().id("id1").word("word1").number("number1").condition("condition1");
    }

    public static SplendVerses getSplendVersesSample2() {
        return new SplendVerses().id("id2").word("word2").number("number2").condition("condition2");
    }

    public static SplendVerses getSplendVersesRandomSampleGenerator() {
        return new SplendVerses()
            .id(UUID.randomUUID().toString())
            .word(UUID.randomUUID().toString())
            .number(UUID.randomUUID().toString())
            .condition(UUID.randomUUID().toString());
    }
}
