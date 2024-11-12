package com.konsol.divinesign.service.mapper.api;

import com.konsol.divinesign.domain.Splend;
import com.konsol.divinesign.domain.SplendVariables;
import com.konsol.divinesign.domain.Verse;
import com.konsol.divinesign.service.api.dto.SplendVariablePayload;
import com.konsol.divinesign.service.api.dto.VersePayload;
import com.konsol.divinesign.service.dto.SplendDTO;
import com.konsol.divinesign.service.mapper.EntityMapper;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Splend} and its DTO {@link SplendDTO}.
 */
@Mapper(componentModel = "spring", uses = { SurahPayloadMapper.class })
public interface SplendVariablePayloadMapper extends EntityMapper<SplendVariablePayload, SplendVariables> {
    @Override
    SplendVariablePayload toDto(SplendVariables entity);
}
