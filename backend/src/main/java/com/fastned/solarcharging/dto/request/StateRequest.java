package com.fastned.solarcharging.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

/**
 * Data Transfer Object for State request
 */
@Data
public class StateRequest {

    private Long id;

    @NotBlank
    private Integer age;

    @NotBlank
    private String name;

    @NotBlank
    private Integer powerOutput;

    private Boolean isFirstState;

    private Date createdAt;

    private Long idSolarGrid;
    
}
