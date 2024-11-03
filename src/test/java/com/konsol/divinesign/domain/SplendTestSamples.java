package com.konsol.divinesign.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class SplendTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Splend getSplendSample1() {
        return new Splend().id("id1").title("title1").likes(1).dislikes(1);
    }

    public static Splend getSplendSample2() {
        return new Splend().id("id2").title("title2").likes(2).dislikes(2);
    }

    public static Splend getSplendRandomSampleGenerator() {
        return new Splend()
            .id(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .likes(intCount.incrementAndGet())
            .dislikes(intCount.incrementAndGet());
    }
}
