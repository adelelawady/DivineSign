package com.konsol.divinesign.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class SurahTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Surah getSurahSample1() {
        return new Surah().id("id1").name("name1").transliteration("transliteration1").type("type1").totalVerses(1);
    }

    public static Surah getSurahSample2() {
        return new Surah().id("id2").name("name2").transliteration("transliteration2").type("type2").totalVerses(2);
    }

    public static Surah getSurahRandomSampleGenerator() {
        return new Surah()
            .id(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .transliteration(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .totalVerses(intCount.incrementAndGet());
    }
}
