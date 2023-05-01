package com.fastned.solarcharging.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for SolarGrid response
 */
@Data
public class SolarGridCreateResponse {

    private Long id;
    private String name;

    private Integer powerOutput;

    private List<StateResponse> responseList = new ArrayList<>();

    /**
     *  @return Produced: [Output] kWh
     * Network:
     * 	[NAME] in use for [AGE] days
     * 	[NAME] in use for [AGE] days
     *
     *
     */
    public String toString() {
        String ret = "Produced: %d kWh\n".formatted(this.powerOutput);

        ret += "Network:\n";
        for ( StateResponse st : responseList ) {
            ret += "%s in use for %d days\n".formatted(st.getName(), st.getAge());
        }

        return ret;
    }

}
