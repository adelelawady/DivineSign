package com.konsol.divinesign.service.mapper;

import com.konsol.divinesign.domain.Category;
import com.konsol.divinesign.domain.Splend;
import com.konsol.divinesign.domain.SplendVariables;
import com.konsol.divinesign.domain.User;
import com.konsol.divinesign.service.dto.CategoryDTO;
import com.konsol.divinesign.service.dto.SplendDTO;
import com.konsol.divinesign.service.dto.SplendVersesDTO;
import com.konsol.divinesign.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Splend} and its DTO {@link SplendDTO}.
 */
@Mapper(componentModel = "spring")
public interface SplendMapper extends EntityMapper<SplendDTO, Splend> {
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    @Mapping(target = "verses", source = "verses", qualifiedByName = "splendVersesId")
    @Mapping(target = "likedUsers", source = "likedUsers", qualifiedByName = "userIdSet")
    @Mapping(target = "dislikedSplends", source = "dislikedSplends", qualifiedByName = "userIdSet")
    SplendDTO toDto(Splend s);

    @Mapping(target = "removeLikedUsers", ignore = true)
    @Mapping(target = "removeDislikedSplend", ignore = true)
    Splend toEntity(SplendDTO splendDTO);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);

    @Named("splendVersesId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SplendVersesDTO toDtoSplendVersesId(SplendVariables splendVariables);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("userIdSet")
    default Set<UserDTO> toDtoUserIdSet(Set<User> user) {
        return user.stream().map(this::toDtoUserId).collect(Collectors.toSet());
    }
}
