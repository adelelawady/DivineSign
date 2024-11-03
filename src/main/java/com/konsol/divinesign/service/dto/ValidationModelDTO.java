package com.konsol.divinesign.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.konsol.divinesign.domain.ValidationModel} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ValidationModelDTO implements Serializable {

    private String id;

    private String name;

    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ValidationModelDTO)) {
            return false;
        }

        ValidationModelDTO validationModelDTO = (ValidationModelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, validationModelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ValidationModelDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
