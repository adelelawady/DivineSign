package com.konsol.divinesign.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class SplendAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSplendAllPropertiesEquals(Splend expected, Splend actual) {
        assertSplendAutoGeneratedPropertiesEquals(expected, actual);
        assertSplendAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSplendAllUpdatablePropertiesEquals(Splend expected, Splend actual) {
        assertSplendUpdatableFieldsEquals(expected, actual);
        assertSplendUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSplendAutoGeneratedPropertiesEquals(Splend expected, Splend actual) {
        assertThat(expected)
            .as("Verify Splend auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSplendUpdatableFieldsEquals(Splend expected, Splend actual) {
        assertThat(expected)
            .as("Verify Splend relevant properties")
            .satisfies(e -> assertThat(e.getTitle()).as("check title").isEqualTo(actual.getTitle()))
            .satisfies(e -> assertThat(e.getContent()).as("check content").isEqualTo(actual.getContent()))
            .satisfies(e -> assertThat(e.getLikes()).as("check likes").isEqualTo(actual.getLikes()))
            .satisfies(e -> assertThat(e.getDislikes()).as("check dislikes").isEqualTo(actual.getDislikes()))
            .satisfies(e -> assertThat(e.getVerified()).as("check verified").isEqualTo(actual.getVerified()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSplendUpdatableRelationshipsEquals(Splend expected, Splend actual) {
        assertThat(expected)
            .as("Verify Splend relationships")
            .satisfies(e -> assertThat(e.getCategory()).as("check category").isEqualTo(actual.getCategory()))
            .satisfies(e -> assertThat(e.getVerses()).as("check verses").isEqualTo(actual.getVerses()));
    }
}
