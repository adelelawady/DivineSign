package com.konsol.divinesign.domain;

import static com.konsol.divinesign.domain.CommentTestSamples.*;
import static com.konsol.divinesign.domain.CommentTestSamples.*;
import static com.konsol.divinesign.domain.SplendTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.divinesign.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CommentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Comment.class);
        Comment comment1 = getCommentSample1();
        Comment comment2 = new Comment();
        assertThat(comment1).isNotEqualTo(comment2);

        comment2.setId(comment1.getId());
        assertThat(comment1).isEqualTo(comment2);

        comment2 = getCommentSample2();
        assertThat(comment1).isNotEqualTo(comment2);
    }

    @Test
    void splendTest() {
        Comment comment = getCommentRandomSampleGenerator();
        Splend splendBack = getSplendRandomSampleGenerator();

        comment.setSplend(splendBack);
        assertThat(comment.getSplend()).isEqualTo(splendBack);

        comment.splend(null);
        assertThat(comment.getSplend()).isNull();
    }

    @Test
    void parentTest() {
        Comment comment = getCommentRandomSampleGenerator();
        Comment commentBack = getCommentRandomSampleGenerator();

        comment.addParent(commentBack);
        assertThat(comment.getParents()).containsOnly(commentBack);

        comment.removeParent(commentBack);
        assertThat(comment.getParents()).doesNotContain(commentBack);

        comment.parents(new HashSet<>(Set.of(commentBack)));
        assertThat(comment.getParents()).containsOnly(commentBack);

        comment.setParents(new HashSet<>());
        assertThat(comment.getParents()).doesNotContain(commentBack);
    }

    @Test
    void commentTest() {
        Comment comment = getCommentRandomSampleGenerator();
        Comment commentBack = getCommentRandomSampleGenerator();

        comment.addComment(commentBack);
        assertThat(comment.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getParents()).containsOnly(comment);

        comment.removeComment(commentBack);
        assertThat(comment.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getParents()).doesNotContain(comment);

        comment.comments(new HashSet<>(Set.of(commentBack)));
        assertThat(comment.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getParents()).containsOnly(comment);

        comment.setComments(new HashSet<>());
        assertThat(comment.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getParents()).doesNotContain(comment);
    }
}
