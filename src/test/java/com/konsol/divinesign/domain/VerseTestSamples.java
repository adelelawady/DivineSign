package com.konsol.divinesign.domain;

import java.util.UUID;

public class VerseTestSamples {

    public static Verse getVerseSample1() {
        return new Verse().id("id1").verse("verse1").diacriticVerse("diacriticVerse1");
    }

    public static Verse getVerseSample2() {
        return new Verse().id("id2").verse("verse2").diacriticVerse("diacriticVerse2");
    }

    public static Verse getVerseRandomSampleGenerator() {
        return new Verse()
            .id(UUID.randomUUID().toString())
            .verse(UUID.randomUUID().toString())
            .diacriticVerse(UUID.randomUUID().toString());
    }
}
