package com.fastned.solarcharging.dto.response;

import lombok.Data;

/**
 * Data Transfer Object for Network response
 */
@Data
public class NetworkResponse {

    private Long id;
    private String name;
    private SolarGridResponse type;
    private UserResponse user;
}
