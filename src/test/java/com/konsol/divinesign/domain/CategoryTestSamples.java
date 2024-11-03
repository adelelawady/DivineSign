package com.konsol.divinesign.domain;

import java.util.UUID;

public class CategoryTestSamples {

    public static Category getCategorySample1() {
        return new Category().id("id1").title("title1");
    }

    public static Category getCategorySample2() {
        return new Category().id("id2").title("title2");
    }

    public static Category getCategoryRandomSampleGenerator() {
        return new Category().id(UUID.randomUUID().toString()).title(UUID.randomUUID().toString());
    }
}
