package com.konsol.divinesign.domain;

import java.util.UUID;

public class TagTestSamples {

    public static Tag getTagSample1() {
        return new Tag().id("id1").title("title1");
    }

    public static Tag getTagSample2() {
        return new Tag().id("id2").title("title2");
    }

    public static Tag getTagRandomSampleGenerator() {
        return new Tag().id(UUID.randomUUID().toString()).title(UUID.randomUUID().toString());
    }
}
