package com.fastned.solarcharging.dto.mapper;

import com.fastned.solarcharging.dto.request.StateRequest;
import com.fastned.solarcharging.model.SolarGrid;
import com.fastned.solarcharging.model.State;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper used for mapping StateRequest fields
 */
@Mapper(componentModel = "spring")
public interface StateRequestMapper {

    State toEntity(StateRequest dto);

    StateRequest toDto(State entity);
}
