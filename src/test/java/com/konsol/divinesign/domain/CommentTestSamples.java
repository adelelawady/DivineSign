package com.konsol.divinesign.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class CommentTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Comment getCommentSample1() {
        return new Comment().id("id1").likes(1);
    }

    public static Comment getCommentSample2() {
        return new Comment().id("id2").likes(2);
    }

    public static Comment getCommentRandomSampleGenerator() {
        return new Comment().id(UUID.randomUUID().toString()).likes(intCount.incrementAndGet());
    }
}
