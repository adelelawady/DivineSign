package com.konsol.divinesign.service.mapper;

import com.konsol.divinesign.domain.Request;
import com.konsol.divinesign.domain.Splend;
import com.konsol.divinesign.domain.User;
import com.konsol.divinesign.service.dto.RequestDTO;
import com.konsol.divinesign.service.dto.SplendDTO;
import com.konsol.divinesign.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Request} and its DTO {@link RequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface RequestMapper extends EntityMapper<RequestDTO, Request> {
    @Mapping(target = "splend", source = "splend", qualifiedByName = "splendId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    RequestDTO toDto(Request s);

    @Named("splendId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SplendDTO toDtoSplendId(Splend splend);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
