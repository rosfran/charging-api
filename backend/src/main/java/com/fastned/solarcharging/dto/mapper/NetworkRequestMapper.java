package com.fastned.solarcharging.dto.mapper;

import com.fastned.solarcharging.dto.request.NetworkRequest;
import com.fastned.solarcharging.model.Network;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper used for mapping NetworkRequest fields
 */
@Mapper(componentModel = "spring")
public interface NetworkRequestMapper {

    @Mapping(target = "name", expression = "java(org.apache.commons.text.WordUtils.capitalizeFully(dto.getName()))")
    Network toEntity(NetworkRequest dto);

    NetworkRequest toDto(Network entity);
}
