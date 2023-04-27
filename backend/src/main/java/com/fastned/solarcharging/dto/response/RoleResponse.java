package com.fastned.solarcharging.dto.response;

import com.fastned.solarcharging.model.RoleType;
import lombok.Data;

/**
 * Data Transfer Object for Role response
 */
@Data
public class RoleResponse {

    private Long id;
    private RoleType type;
}
