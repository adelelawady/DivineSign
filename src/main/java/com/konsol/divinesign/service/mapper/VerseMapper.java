package com.konsol.divinesign.service.mapper;

import com.konsol.divinesign.domain.Surah;
import com.konsol.divinesign.domain.Verse;
import com.konsol.divinesign.service.dto.SurahDTO;
import com.konsol.divinesign.service.dto.VerseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Verse} and its DTO {@link VerseDTO}.
 */
@Mapper(componentModel = "spring")
public interface VerseMapper extends EntityMapper<VerseDTO, Verse> {
    @Mapping(target = "surah", source = "surah", qualifiedByName = "surahId")
    VerseDTO toDto(Verse s);

    @Named("surahId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SurahDTO toDtoSurahId(Surah surah);
}
