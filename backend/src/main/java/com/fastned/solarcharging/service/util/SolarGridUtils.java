package com.fastned.solarcharging.service.util;

import com.fastned.solarcharging.dto.request.NetworkRequest;
import com.fastned.solarcharging.dto.request.SolarGridRequest;
import com.fastned.solarcharging.dto.response.CommandResponse;
import com.fastned.solarcharging.dto.response.NetworkCreateResponse;
import com.fastned.solarcharging.dto.response.SolarGridResponse;
import com.fastned.solarcharging.dto.response.UserResponse;
import com.fastned.solarcharging.service.NetworkService;
import com.fastned.solarcharging.service.SolarGridService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j(topic = "SolarGridUtils")
public class SolarGridUtils {

    public static final int DAYS_IN_A_YEAR = 365;
    public static final int DAYS_POWER_PRODUCTION_ON_HOLD = 60;

    /**
     * Calculate the power output generated for a given SolarGrid
     *
     * Solar grids have to be installed first, so only start to produce energy after 60 days of placement (age 0 is day of placement).
     *
     * @param days
     * @return The calculated amount of power generated, or 0 in case the
     */
    public static double calculatePowerOutput(int days) {
        if ( days  > DAYS_POWER_PRODUCTION_ON_HOLD )
            return 20*(1-( ( days - DAYS_POWER_PRODUCTION_ON_HOLD)/ DAYS_IN_A_YEAR *0.005));
        else
            return 0;
    }

    public static NetworkCreateResponse processIncomingNetworkFile(NetworkService networkService, SolarGridService solarGridService, List<Object> list, UserResponse userResponse, Integer elapsedTimeDays) {
        NetworkCreateResponse response = new NetworkCreateResponse();

        NetworkRequest network = new NetworkRequest();
        network.setName(userResponse.getUsername());
        network.setIdUser(userResponse.getId());
        CommandResponse net = networkService.create(network);

        for (final Object o: list) {
            if (o instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) o;
                log.info("Items found: " + map.size());
                int i = 0;

                SolarGridRequest solRequest = new SolarGridRequest();

                for (Map.Entry<String,Object> entry: map.entrySet()) {
                    log.info(entry.getKey() + " = " + entry.getValue());

                    if ( entry.getKey().equalsIgnoreCase("name") ) {
                        solRequest.setName((String) entry.getValue());
                    } else if ( entry.getKey().equalsIgnoreCase("age")) {
                        int value = -1;
                        value = (Integer)entry.getValue();
                        solRequest.setAge(value);
                    }

                    i++;
                }

                solRequest.setIdNetwork(net.id());

                SolarGridResponse solarGridResponse = solarGridService.create(solRequest);

                response.getResponseList().add( solarGridResponse );

                // sum up all the local SolarGrid power output
                if ( elapsedTimeDays != null && elapsedTimeDays > 0 )
                    response.setPowerOutput( response.getPowerOutput() + SolarGridUtils.calculatePowerOutput(solarGridResponse.getAge()) );
                else
                    response.setPowerOutput( response.getPowerOutput() + solarGridResponse.getPowerOutput() );
            }
        }

        return response;
    }


    public static NetworkCreateResponse processIncomingNetwork(NetworkService networkService, SolarGridService solarGridService, List<SolarGridRequest> list, UserResponse userResponse, Integer elapsedTimeDays) {
        NetworkCreateResponse response = new NetworkCreateResponse();
        NetworkRequest network = new NetworkRequest();
        network.setName(userResponse.getUsername());
        network.setIdUser(userResponse.getId());
        CommandResponse net = networkService.create(network);

        for (final SolarGridRequest solRequest: list) {
                solRequest.setIdNetwork(net.id());

                SolarGridResponse solarGridResponse = solarGridService.create(solRequest);

                response.getResponseList().add( solarGridResponse );

                // sum up all the local SolarGrid power output
                if ( elapsedTimeDays != null && elapsedTimeDays > 0 )
                    response.setPowerOutput( response.getPowerOutput() + SolarGridUtils.calculatePowerOutput(solarGridResponse.getAge()) );
                else
                    response.setPowerOutput( response.getPowerOutput() + solarGridResponse.getPowerOutput() );
            }


        return response;
    }



}
