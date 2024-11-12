package com.konsol.divinesign.service.mapper;

import com.konsol.divinesign.domain.SplendVariables;
import com.konsol.divinesign.domain.ValidationModel;
import com.konsol.divinesign.service.dto.SplendVersesDTO;
import com.konsol.divinesign.service.dto.ValidationModelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SplendVariables} and its DTO {@link SplendVersesDTO}.
 */
@Mapper(componentModel = "spring")
public interface SplendVariablesMapper extends EntityMapper<SplendVersesDTO, SplendVariables> {
    SplendVersesDTO toDto(SplendVariables s);

    @Named("validationModelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ValidationModelDTO toDtoValidationModelId(ValidationModel validationModel);
}
