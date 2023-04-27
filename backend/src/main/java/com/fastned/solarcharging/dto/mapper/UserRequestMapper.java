package com.fastned.solarcharging.dto.mapper;

import com.fastned.solarcharging.dto.request.UserRequest;
import com.fastned.solarcharging.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper used for mapping UserRequest fields
 */
@Mapper(componentModel = "spring")
public interface UserRequestMapper {

    @Mapping(target = "firstName", expression = "java(org.apache.commons.text.WordUtils.capitalizeFully(dto.getFirstName()))")
    @Mapping(target = "lastName", expression = "java(org.apache.commons.text.WordUtils.capitalizeFully(dto.getLastName()))")
    @Mapping(target = "username", expression = "java(dto.getUsername().trim().toLowerCase())")
    @Mapping(source = "password", target = "password", ignore = true)
    @Mapping(source = "roles", target = "roles", ignore = true)
    User toEntity(UserRequest dto);

    @Mapping(source = "password", target = "password", ignore = true)
    @Mapping(source = "roles", target = "roles", ignore = true)
    UserRequest toDto(User entity);
}
