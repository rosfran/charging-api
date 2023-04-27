package com.fastned.solarcharging.dto.mapper;

import com.fastned.solarcharging.dto.response.SolarGridResponse;
import com.fastned.solarcharging.model.SolarGrid;
import org.mapstruct.Mapper;

/**
 * Mapper used for mapping SolarGridResponse fields
 */
@Mapper(componentModel = "spring")
public interface SolarGridResponseMapper {

    SolarGrid toEntity(SolarGridResponse dto);

    SolarGridResponse toDto(SolarGrid entity);
}
