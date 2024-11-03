package com.konsol.divinesign.service.mapper;

import com.konsol.divinesign.domain.Surah;
import com.konsol.divinesign.service.dto.SurahDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Surah} and its DTO {@link SurahDTO}.
 */
@Mapper(componentModel = "spring")
public interface SurahMapper extends EntityMapper<SurahDTO, Surah> {}
