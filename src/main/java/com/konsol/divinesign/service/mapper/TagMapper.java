package com.konsol.divinesign.service.mapper;

import com.konsol.divinesign.domain.Splend;
import com.konsol.divinesign.domain.Tag;
import com.konsol.divinesign.service.dto.SplendDTO;
import com.konsol.divinesign.service.dto.TagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring")
public interface TagMapper extends EntityMapper<TagDTO, Tag> {
    @Mapping(target = "splend", source = "splend", qualifiedByName = "splendId")
    TagDTO toDto(Tag s);

    @Named("splendId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SplendDTO toDtoSplendId(Splend splend);
}
