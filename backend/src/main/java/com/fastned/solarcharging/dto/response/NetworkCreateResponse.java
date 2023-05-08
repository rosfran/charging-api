package com.fastned.solarcharging.dto.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for SolarGrid response
 */
@Data
public class NetworkCreateResponse {

    private Long id;
    private String name;

    private Double powerOutput;

    private List<SolarGridResponse> responseList = new ArrayList<>();

    public NetworkCreateResponse() {
        this.powerOutput = 0.0;
    }

    /**
     *  @return Produced: [Output] kWh
     * Network:
     * 	[NAME] in use for [AGE] days
     * 	[NAME] in use for [AGE] days
     *
     *
     */
    public String toString() {
        String ret = "Produced: %,.2f kWh\n".formatted(this.powerOutput);

        ret += "Network:\n";
        for ( SolarGridResponse st : responseList ) {
            ret += "%s in use for %d days\n".formatted(st.getName(), st.getAge());
        }

        return ret;
    }

}
