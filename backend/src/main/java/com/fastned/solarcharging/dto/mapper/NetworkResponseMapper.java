package com.fastned.solarcharging.dto.mapper;

import com.fastned.solarcharging.dto.response.NetworkResponse;
import com.fastned.solarcharging.model.Network;
import org.mapstruct.Mapper;

/**
 * Mapper used for mapping NetworkResponse fields
 */
@Mapper(componentModel = "spring")
public interface NetworkResponseMapper {

    Network toEntity(NetworkResponse dto);

    NetworkResponse toDto(Network entity);
}
