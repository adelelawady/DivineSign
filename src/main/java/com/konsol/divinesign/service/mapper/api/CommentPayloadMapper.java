package com.konsol.divinesign.service.mapper.api;

import com.konsol.divinesign.domain.Comment;
import com.konsol.divinesign.domain.Splend;
import com.konsol.divinesign.domain.User;
import com.konsol.divinesign.domain.Verse;
import com.konsol.divinesign.service.api.dto.CommentViewPayload;
import com.konsol.divinesign.service.api.dto.UserViewPayload;
import com.konsol.divinesign.service.api.dto.VersePayload;
import com.konsol.divinesign.service.dto.SplendDTO;
import com.konsol.divinesign.service.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Splend} and its DTO {@link SplendDTO}.
 */
@Mapper(componentModel = "spring", uses = { SplendPayloadMapper.class })
public interface CommentPayloadMapper extends EntityMapper<CommentViewPayload, Comment> {
    @Override
    CommentViewPayload toDto(Comment entity);

    @Override
    @Mapping(target = "user", ignore = true)
    Comment toEntity(CommentViewPayload dto);
}
