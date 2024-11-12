package com.konsol.divinesign.service.mapper.api;

import com.konsol.divinesign.domain.Splend;
import com.konsol.divinesign.domain.Tag;
import com.konsol.divinesign.domain.User;
import com.konsol.divinesign.repository.TagRepository;
import com.konsol.divinesign.service.TagService;
import com.konsol.divinesign.service.api.dto.SplendViewPayload;
import com.konsol.divinesign.service.api.dto.UserViewPayload;
import com.konsol.divinesign.service.dto.SplendDTO;
import com.konsol.divinesign.service.impl.TagServiceImpl;
import com.konsol.divinesign.service.mapper.EntityMapper;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper for the entity {@link Splend} and its DTO {@link SplendDTO}.
 */
@Mapper(componentModel = "spring", uses = { SplendVariablePayloadMapper.class })
public interface SplendPayloadMapper extends EntityMapper<SplendViewPayload, Splend> {
    @Override
    SplendViewPayload toDto(Splend entity);

    @Override
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    Splend toEntity(SplendViewPayload dto);

    default UserViewPayload fromUserToUserPayload(User tagSet) {
        return new UserViewPayload().id(tagSet.getId()).name(tagSet.getFirstName());
    }

    default List<String> fromTags(Set<Tag> tagSet) {
        return tagSet.stream().map(Tag::getTitle).toList();
    }

    default OffsetDateTime map(Instant value) {
        return OffsetDateTime.ofInstant(value, java.time.ZoneOffset.UTC);
    }

    default Instant map(OffsetDateTime value) {
        return value.toInstant();
    }

    default Set<Tag> map(List<String> value) {
        return new LinkedHashSet<>();
    }
}
