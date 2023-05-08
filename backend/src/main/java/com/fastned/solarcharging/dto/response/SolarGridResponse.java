package com.fastned.solarcharging.dto.response;

import lombok.Data;

import java.util.Date;

/**
 * Data Transfer Object for SolarGrid response
 */
@Data
public class SolarGridResponse {

    private Long id;
    private String name;

    public SolarGridResponse(){

    }

    public SolarGridResponse( Integer age, String name ) {
        this.name = name;
        this.age = age;
    }

    private Integer age;

    private Integer powerOutput;

    private Boolean isFirstState;

    private Date createdAt;
}
