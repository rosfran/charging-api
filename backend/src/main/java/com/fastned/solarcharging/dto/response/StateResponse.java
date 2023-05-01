package com.fastned.solarcharging.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

/**
 * Data Transfer Object for State request
 */
@Data
public class StateResponse {

    public StateResponse() {

    }

    public StateResponse( Integer age, String name ) {
        this.name = name;
        this.age = age;
    }

    private Long id;

    private Integer age;

    private String name;

    private Integer powerOutput;

    private Boolean isFirstState;

    private Date createdAt;
    
}
