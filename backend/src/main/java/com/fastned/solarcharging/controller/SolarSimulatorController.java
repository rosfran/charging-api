package com.fastned.solarcharging.controller;

import com.fastned.solarcharging.dto.request.NetworkRequest;
import com.fastned.solarcharging.dto.request.SolarGridRequest;
import com.fastned.solarcharging.dto.response.*;
import com.fastned.solarcharging.service.NetworkService;
import com.fastned.solarcharging.service.SolarGridService;
import com.fastned.solarcharging.service.UserService;
import com.fastned.solarcharging.service.util.SolarGridUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.fastned.solarcharging.common.Constants.SUCCESS;

@Slf4j(topic = "SolarSimulatorController")
@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/v1/solar-simulator")
@RequiredArgsConstructor
public class SolarSimulatorController {

    private final Clock clock;
    private final NetworkService networkService;

    private final SolarGridService solarGridService;
    private final UserService userService;

    /**
     * FAST-2: As a user, I want to be able to load my network into the application using an HTTP REST service.
     *
     * In that case, the SolarGrid list will be sent in the HTTP Request payload
     *
     * @param solarGrid
     * @param auth
     * @param redirectAttributes
     * @return
     */
    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @PostMapping("/load")
    public ResponseEntity<ApiResponse<String>>  handleSolarGridStateFileUpload(@RequestBody List<SolarGridRequest> solarGrid,
                                                                               Authentication auth,
                                                                               RedirectAttributes redirectAttributes)  {


        UserDetails user = (UserDetails)auth.getPrincipal();

        UserResponse userResponse = userService.findByName(user.getUsername());

        NetworkCreateResponse response = SolarGridUtils.processIncomingNetwork(networkService, solarGridService, solarGrid, userResponse, null);

        return ResponseEntity.status(HttpStatus.RESET_CONTENT).body(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response.toString()));
    }

    /**
     * Fetches all Solar Grid output based on the given userId
     *
     * FAST-3: As a user, I want to be able to query my network and its total output over time using a HTTP REST services which output JSON data
     * The following are the requests you wish to make.
     * • GET /solar-simulator/output/T: Which returns a result of your total output at T days
     *
     * @param days
     * @return List of NetworkResponse
     */
    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @GetMapping("/output/{days}")
    public ResponseEntity<ApiResponse<SolarSimulatorTotalOutputResponse>> generateOutputDuringDays(@PathVariable Integer days, Authentication auth) {

        SolarSimulatorTotalOutputResponse solarSimulatorTotalOutputResponse = new SolarSimulatorTotalOutputResponse();

        // first, check if the total of days is more than 60 days, because before that limit none of the solar grids can produce any power output
        if ( days > SolarGridUtils.DAYS_POWER_PRODUCTION_ON_HOLD ) {
            UserDetails user = (UserDetails) auth.getPrincipal();
            UserResponse userResponse = userService.findByName(user.getUsername());

            final List<SolarGridResponse> responseList = solarGridService.findByUserId(userResponse.getId());

            for (final SolarGridResponse s : responseList) {
                // It only sums values from SolarGrids in the case they are working for at least 60 days
                if ( s.getAge() > SolarGridUtils.DAYS_POWER_PRODUCTION_ON_HOLD )
                    solarSimulatorTotalOutputResponse.setTotalOutputInKWh(solarSimulatorTotalOutputResponse.getTotalOutputInKWh() + SolarGridUtils.calculatePowerOutput(days));
            }
        }

        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, solarSimulatorTotalOutputResponse));
    }

    /**
     * Fetches all Solar Grid list based on the given userId
     *
     * FAST-3: As a user, I want to be able to query my network and its total output over time using a HTTP REST services which output JSON data
     * The following are the requests you wish to make.
     * • GET /solar-simulator/network/T: Which returns a result of your network at T days
     *
     * @param days
     * @return List of NetworkResponse
     */
    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @GetMapping("/network/{days}")
    public ResponseEntity<ApiResponse<List<SolarGridResponse>>> generateNetworkDuringDays(@PathVariable Integer days, Authentication auth) {
        List<SolarGridResponse> responseList = new ArrayList<>();
        // first, check if the total of days is more than 60 days, because before that limit none of the solar grids can produce any power output
        if ( days > SolarGridUtils.DAYS_POWER_PRODUCTION_ON_HOLD ) {
            UserDetails user = (UserDetails) auth.getPrincipal();
            UserResponse userResponse = userService.findByName(user.getUsername());

           responseList = solarGridService.findByUserId(userResponse.getId());

            for (final SolarGridResponse s : responseList) {
                s.setPowerOutput(s.getPowerOutput() + SolarGridUtils.calculatePowerOutput(days));
            }
        }

        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, responseList));
    }


}
