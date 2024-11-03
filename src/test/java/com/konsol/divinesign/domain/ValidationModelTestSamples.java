package com.konsol.divinesign.domain;

import java.util.UUID;

public class ValidationModelTestSamples {

    public static ValidationModel getValidationModelSample1() {
        return new ValidationModel().id("id1").name("name1").type("type1");
    }

    public static ValidationModel getValidationModelSample2() {
        return new ValidationModel().id("id2").name("name2").type("type2");
    }

    public static ValidationModel getValidationModelRandomSampleGenerator() {
        return new ValidationModel().id(UUID.randomUUID().toString()).name(UUID.randomUUID().toString()).type(UUID.randomUUID().toString());
    }
}
