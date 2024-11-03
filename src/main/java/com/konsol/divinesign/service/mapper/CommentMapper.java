package com.konsol.divinesign.service.mapper;

import com.konsol.divinesign.domain.Comment;
import com.konsol.divinesign.domain.Splend;
import com.konsol.divinesign.domain.User;
import com.konsol.divinesign.service.dto.CommentDTO;
import com.konsol.divinesign.service.dto.SplendDTO;
import com.konsol.divinesign.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comment} and its DTO {@link CommentDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {
    @Mapping(target = "splend", source = "splend", qualifiedByName = "splendId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "parents", source = "parents", qualifiedByName = "commentIdSet")
    CommentDTO toDto(Comment s);

    @Mapping(target = "removeParent", ignore = true)
    Comment toEntity(CommentDTO commentDTO);

    @Named("splendId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SplendDTO toDtoSplendId(Splend splend);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("commentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CommentDTO toDtoCommentId(Comment comment);

    @Named("commentIdSet")
    default Set<CommentDTO> toDtoCommentIdSet(Set<Comment> comment) {
        return comment.stream().map(this::toDtoCommentId).collect(Collectors.toSet());
    }
}
