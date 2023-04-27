package com.fastned.solarcharging.dto.mapper;

import com.fastned.solarcharging.dto.response.UserResponse;
import com.fastned.solarcharging.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.text.MessageFormat;

/**
 * Mapper used for mapping UserResponse fields
 */
@Mapper(componentModel = "spring")
public interface UserResponseMapper {

    User toEntity(UserResponse dto);

    UserResponse toDto(User entity);

    @AfterMapping
    default void setFullName(@MappingTarget UserResponse dto, User entity) {
        dto.setFullName(MessageFormat.format("{0} {1}", entity.getFirstName(), entity.getLastName()));
    }
}
