package com.konsol.divinesign.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidationModelAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertValidationModelAllPropertiesEquals(ValidationModel expected, ValidationModel actual) {
        assertValidationModelAutoGeneratedPropertiesEquals(expected, actual);
        assertValidationModelAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertValidationModelAllUpdatablePropertiesEquals(ValidationModel expected, ValidationModel actual) {
        assertValidationModelUpdatableFieldsEquals(expected, actual);
        assertValidationModelUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertValidationModelAutoGeneratedPropertiesEquals(ValidationModel expected, ValidationModel actual) {
        assertThat(expected)
            .as("Verify ValidationModel auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertValidationModelUpdatableFieldsEquals(ValidationModel expected, ValidationModel actual) {
        assertThat(expected)
            .as("Verify ValidationModel relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getType()).as("check type").isEqualTo(actual.getType()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertValidationModelUpdatableRelationshipsEquals(ValidationModel expected, ValidationModel actual) {
        // empty method
    }
}
