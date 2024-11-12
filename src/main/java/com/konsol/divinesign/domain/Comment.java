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
 * A Comment.
 */
@Document(collection = "comment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Comment extends AbstractAuditingEntity<String> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("content")
    private String content;

    @NotNull
    @Field("likes")
    private Integer likes;

    @Field("deleted")
    private Boolean deleted;

    @DBRef
    @Field("splend")
    @JsonIgnoreProperties(value = { "category", "verses", "tags", "likedUsers", "dislikedSplends" }, allowSetters = true)
    private Splend splend;

    @DBRef
    @Field("user")
    private User user;

    @DBRef
    @Field("parents")
    @JsonIgnoreProperties(value = { "splend", "user", "parents", "comments" }, allowSetters = true)
    private Set<Comment> parents = new HashSet<>();

    @DBRef
    @Field("comments")
    @JsonIgnoreProperties(value = { "splend", "user", "parents", "comments" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Comment id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public Comment content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikes() {
        return this.likes;
    }

    public Comment likes(Integer likes) {
        this.setLikes(likes);
        return this;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Comment deleted(Boolean deleted) {
        this.setDeleted(deleted);
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Splend getSplend() {
        return this.splend;
    }

    public void setSplend(Splend splend) {
        this.splend = splend;
    }

    public Comment splend(Splend splend) {
        this.setSplend(splend);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Comment> getParents() {
        return this.parents;
    }

    public void setParents(Set<Comment> comments) {
        this.parents = comments;
    }

    public Comment parents(Set<Comment> comments) {
        this.setParents(comments);
        return this;
    }

    public Comment addParent(Comment comment) {
        this.parents.add(comment);
        return this;
    }

    public Comment removeParent(Comment comment) {
        this.parents.remove(comment);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.removeParent(this));
        }
        if (comments != null) {
            comments.forEach(i -> i.addParent(this));
        }
        this.comments = comments;
    }

    public Comment comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Comment addComment(Comment comment) {
        this.comments.add(comment);
        comment.getParents().add(this);
        return this;
    }

    public Comment removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.getParents().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comment)) {
            return false;
        }
        return getId() != null && getId().equals(((Comment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Comment{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", likes=" + getLikes() +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
