package com.konsol.divinesign.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class SplendVersesAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSplendVersesAllPropertiesEquals(SplendVerses expected, SplendVerses actual) {
        assertSplendVersesAutoGeneratedPropertiesEquals(expected, actual);
        assertSplendVersesAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSplendVersesAllUpdatablePropertiesEquals(SplendVerses expected, SplendVerses actual) {
        assertSplendVersesUpdatableFieldsEquals(expected, actual);
        assertSplendVersesUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSplendVersesAutoGeneratedPropertiesEquals(SplendVerses expected, SplendVerses actual) {
        assertThat(expected)
            .as("Verify SplendVerses auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSplendVersesUpdatableFieldsEquals(SplendVerses expected, SplendVerses actual) {
        assertThat(expected)
            .as("Verify SplendVerses relevant properties")
            .satisfies(e -> assertThat(e.getWord()).as("check word").isEqualTo(actual.getWord()))
            .satisfies(e -> assertThat(e.getNumber()).as("check number").isEqualTo(actual.getNumber()))
            .satisfies(e -> assertThat(e.getCondition()).as("check condition").isEqualTo(actual.getCondition()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSplendVersesUpdatableRelationshipsEquals(SplendVerses expected, SplendVerses actual) {
        assertThat(expected)
            .as("Verify SplendVerses relationships")
            .satisfies(e -> assertThat(e.getValidationMethod()).as("check validationMethod").isEqualTo(actual.getValidationMethod()));
    }
}