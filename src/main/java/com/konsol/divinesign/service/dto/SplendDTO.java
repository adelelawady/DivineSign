package com.konsol.divinesign.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.konsol.divinesign.domain.Splend} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SplendDTO implements Serializable {

    private String id;

    @NotNull
    private String title;

    private String content;

    @NotNull
    private Integer likes;

    @NotNull
    private Integer dislikes;

    @NotNull
    private Boolean verified;

    private CategoryDTO category;

    private SplendVersesDTO verses;

    private Set<UserDTO> likedUsers = new HashSet<>();

    private Set<UserDTO> dislikedSplends = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Integer getDislikes() {
        return dislikes;
    }

    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public SplendVersesDTO getVerses() {
        return verses;
    }

    public void setVerses(SplendVersesDTO verses) {
        this.verses = verses;
    }

    public Set<UserDTO> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(Set<UserDTO> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public Set<UserDTO> getDislikedSplends() {
        return dislikedSplends;
    }

    public void setDislikedSplends(Set<UserDTO> dislikedSplends) {
        this.dislikedSplends = dislikedSplends;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SplendDTO)) {
            return false;
        }

        SplendDTO splendDTO = (SplendDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, splendDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SplendDTO{" +
            "id='" + getId() + "'" +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", likes=" + getLikes() +
            ", dislikes=" + getDislikes() +
            ", verified='" + getVerified() + "'" +
            ", category=" + getCategory() +
            ", verses=" + getVerses() +
            ", likedUsers=" + getLikedUsers() +
            ", dislikedSplends=" + getDislikedSplends() +
            "}";
    }
}
