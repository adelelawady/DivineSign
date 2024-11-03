package com.konsol.divinesign.domain;

import java.util.UUID;

public class RequestTestSamples {

    public static Request getRequestSample1() {
        return new Request().id("id1").title("title1");
    }

    public static Request getRequestSample2() {
        return new Request().id("id2").title("title2");
    }

    public static Request getRequestRandomSampleGenerator() {
        return new Request().id(UUID.randomUUID().toString()).title(UUID.randomUUID().toString());
    }
}
