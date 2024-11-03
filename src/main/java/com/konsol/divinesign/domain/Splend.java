package com.konsol.divinesign.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Splend.
 */
@Document(collection = "splend")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Splend implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("title")
    private String title;

    @Field("content")
    private String content;

    @NotNull
    @Field("likes")
    private Integer likes;

    @NotNull
    @Field("dislikes")
    private Integer dislikes;

    @NotNull
    @Field("verified")
    private Boolean verified;

    @DBRef
    @Field("category")
    private Category category;

    @DBRef
    @Field("verses")
    private SplendVerses verses;

    @DBRef
    @Field("tags")
    @JsonIgnoreProperties(value = { "splend" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    @DBRef
    @Field("likedUsers")
    private Set<User> likedUsers = new HashSet<>();

    @DBRef
    @Field("dislikedSplends")
    private Set<User> dislikedSplends = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Splend id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Splend title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public Splend content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikes() {
        return this.likes;
    }

    public Splend likes(Integer likes) {
        this.setLikes(likes);
        return this;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getDislikes() {
        return this.dislikes;
    }

    public Splend dislikes(Integer dislikes) {
        this.setDislikes(dislikes);
        return this;
    }

    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
    }

    public Boolean getVerified() {
        return this.verified;
    }

    public Splend verified(Boolean verified) {
        this.setVerified(verified);
        return this;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Splend category(Category category) {
        this.setCategory(category);
        return this;
    }

    public SplendVerses getVerses() {
        return this.verses;
    }

    public void setVerses(SplendVerses splendVerses) {
        this.verses = splendVerses;
    }

    public Splend verses(SplendVerses splendVerses) {
        this.setVerses(splendVerses);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        if (this.tags != null) {
            this.tags.forEach(i -> i.setSplend(null));
        }
        if (tags != null) {
            tags.forEach(i -> i.setSplend(this));
        }
        this.tags = tags;
    }

    public Splend tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Splend addTags(Tag tag) {
        this.tags.add(tag);
        tag.setSplend(this);
        return this;
    }

    public Splend removeTags(Tag tag) {
        this.tags.remove(tag);
        tag.setSplend(null);
        return this;
    }

    public Set<User> getLikedUsers() {
        return this.likedUsers;
    }

    public void setLikedUsers(Set<User> users) {
        this.likedUsers = users;
    }

    public Splend likedUsers(Set<User> users) {
        this.setLikedUsers(users);
        return this;
    }

    public Splend addLikedUsers(User user) {
        this.likedUsers.add(user);
        return this;
    }

    public Splend removeLikedUsers(User user) {
        this.likedUsers.remove(user);
        return this;
    }

    public Set<User> getDislikedSplends() {
        return this.dislikedSplends;
    }

    public void setDislikedSplends(Set<User> users) {
        this.dislikedSplends = users;
    }

    public Splend dislikedSplends(Set<User> users) {
        this.setDislikedSplends(users);
        return this;
    }

    public Splend addDislikedSplend(User user) {
        this.dislikedSplends.add(user);
        return this;
    }

    public Splend removeDislikedSplend(User user) {
        this.dislikedSplends.remove(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Splend)) {
            return false;
        }
        return getId() != null && getId().equals(((Splend) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Splend{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", likes=" + getLikes() +
            ", dislikes=" + getDislikes() +
            ", verified='" + getVerified() + "'" +
            "}";
    }
}
