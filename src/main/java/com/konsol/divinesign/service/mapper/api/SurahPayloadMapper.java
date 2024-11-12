package com.konsol.divinesign.service.mapper.api;

import com.konsol.divinesign.domain.Splend;
import com.konsol.divinesign.domain.Surah;
import com.konsol.divinesign.service.api.dto.SurahPayload;
import com.konsol.divinesign.service.api.dto.VersePayload;
import com.konsol.divinesign.service.dto.SplendDTO;
import com.konsol.divinesign.service.mapper.EntityMapper;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Splend} and its DTO {@link SplendDTO}.
 */
@Mapper(componentModel = "spring")
public interface SurahPayloadMapper extends EntityMapper<SurahPayload, Surah> {
    @Override
    SurahPayload toDto(Surah entity);
}
