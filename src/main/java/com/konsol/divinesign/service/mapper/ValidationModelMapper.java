package com.konsol.divinesign.service.mapper;

import com.konsol.divinesign.domain.ValidationModel;
import com.konsol.divinesign.service.dto.ValidationModelDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ValidationModel} and its DTO {@link ValidationModelDTO}.
 */
@Mapper(componentModel = "spring")
public interface ValidationModelMapper extends EntityMapper<ValidationModelDTO, ValidationModel> {}
