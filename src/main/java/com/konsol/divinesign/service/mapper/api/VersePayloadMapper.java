package com.konsol.divinesign.service.mapper.api;

import com.konsol.divinesign.domain.Splend;
import com.konsol.divinesign.domain.Tag;
import com.konsol.divinesign.domain.Verse;
import com.konsol.divinesign.service.api.dto.SplendViewPayload;
import com.konsol.divinesign.service.api.dto.VersePayload;
import com.konsol.divinesign.service.dto.SplendDTO;
import com.konsol.divinesign.service.mapper.EntityMapper;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Splend} and its DTO {@link SplendDTO}.
 */
@Mapper(componentModel = "spring", uses = { SurahPayloadMapper.class })
public interface VersePayloadMapper extends EntityMapper<VersePayload, Verse> {
    @Override
    VersePayload toDto(Verse entity);
}
