package com.konsol.divinesign.service.mapper;

import com.konsol.divinesign.domain.SplendVerses;
import com.konsol.divinesign.domain.ValidationModel;
import com.konsol.divinesign.service.dto.SplendVersesDTO;
import com.konsol.divinesign.service.dto.ValidationModelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SplendVerses} and its DTO {@link SplendVersesDTO}.
 */
@Mapper(componentModel = "spring")
public interface SplendVersesMapper extends EntityMapper<SplendVersesDTO, SplendVerses> {
    @Mapping(target = "validationMethod", source = "validationMethod", qualifiedByName = "validationModelId")
    SplendVersesDTO toDto(SplendVerses s);

    @Named("validationModelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ValidationModelDTO toDtoValidationModelId(ValidationModel validationModel);
}
