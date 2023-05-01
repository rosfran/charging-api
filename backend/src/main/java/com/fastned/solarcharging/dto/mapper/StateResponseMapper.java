package com.fastned.solarcharging.dto.mapper;

import com.fastned.solarcharging.dto.response.SolarGridResponse;
import com.fastned.solarcharging.dto.response.StateResponse;
import com.fastned.solarcharging.model.SolarGrid;
import com.fastned.solarcharging.model.State;
import org.mapstruct.Mapper;

/**
 * Mapper used for mapping SolarGridResponse fields
 */
@Mapper(componentModel = "spring")
public interface StateResponseMapper {

    State toEntity(StateResponse dto);

    StateResponse toDto(State entity);
}
