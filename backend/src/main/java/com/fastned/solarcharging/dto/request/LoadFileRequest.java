package com.fastned.solarcharging.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object for LoadFile (api/v1/ request
 */
@Data
public class LoadFileRequest {

    @NotBlank
    private Integer timeElapsedDays;

}