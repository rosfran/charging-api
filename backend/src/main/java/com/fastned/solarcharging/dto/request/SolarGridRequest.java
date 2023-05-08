package com.fastned.solarcharging.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

/**
 * Data Transfer Object for SolarGrid request
 */
@Data
public class SolarGridRequest {

    private Long id;


    @NotBlank
    @Size(min = 3, max = 500)
    private String name;


    @NotBlank
    private Integer age;

    @NotBlank
    private Integer powerOutput;

    private Boolean isFirstState;

    private Date createdAt;

    private Long idNetwork;

}
