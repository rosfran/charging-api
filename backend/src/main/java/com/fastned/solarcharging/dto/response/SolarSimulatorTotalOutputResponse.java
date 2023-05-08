package com.fastned.solarcharging.dto.response;

import lombok.Data;

import java.util.Date;

/**
 * Data Transfer Object for SolarSimulator /api/v1/solar-simulator/outpur/T response
 */
@Data
public class SolarSimulatorTotalOutputResponse {

    private Double totalOutputInKWh = 0.0;

}
