package com.konsol.divinesign.service.mapper;

import com.konsol.divinesign.domain.Category;
import com.konsol.divinesign.domain.ValidationModel;
import com.konsol.divinesign.service.dto.CategoryDTO;
import com.konsol.divinesign.service.dto.ValidationModelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {
    @Mapping(target = "validationMethod", source = "validationMethod", qualifiedByName = "validationModelId")
    CategoryDTO toDto(Category s);

    @Named("validationModelId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ValidationModelDTO toDtoValidationModelId(ValidationModel validationModel);
}
