package com.konsol.divinesign.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.konsol.divinesign.domain.Comment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CommentDTO implements Serializable {

    private String id;

    private String content;

    @NotNull
    private Integer likes;

    private Boolean deleted;

    private SplendDTO splend;

    private UserDTO user;

    private Set<CommentDTO> parents = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public SplendDTO getSplend() {
        return splend;
    }

    public void setSplend(SplendDTO splend) {
        this.splend = splend;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Set<CommentDTO> getParents() {
        return parents;
    }

    public void setParents(Set<CommentDTO> parents) {
        this.parents = parents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentDTO)) {
            return false;
        }

        CommentDTO commentDTO = (CommentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommentDTO{" +
            "id='" + getId() + "'" +
            ", content='" + getContent() + "'" +
            ", likes=" + getLikes() +
            ", deleted='" + getDeleted() + "'" +
            ", splend=" + getSplend() +
            ", user=" + getUser() +
            ", parents=" + getParents() +
            "}";
    }
}
