package com.fastned.solarcharging.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object for SolarGrid request
 */
@Data
public class SolarGridRequest {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 500)
    private String name;

    @Size(min = 3, max = 50)
    private String description;
}
