package com.fastned.solarcharging.dto.mapper;

import com.fastned.solarcharging.model.SolarGrid;
import com.fastned.solarcharging.dto.request.SolarGridRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper used for mapping SolarGridRequest fields
 */
@Mapper(componentModel = "spring")
public interface SolarGridRequestMapper {

    @Mapping(target = "name", expression = "java(org.apache.commons.text.WordUtils.capitalizeFully(dto.getName()))")
    SolarGrid toEntity(SolarGridRequest dto);

    SolarGridRequest toDto(SolarGrid entity);
}
