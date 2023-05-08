package com.fastned.solarcharging.controller;

import com.fastned.solarcharging.dto.request.NetworkRequest;
import com.fastned.solarcharging.dto.request.SolarGridRequest;
import com.fastned.solarcharging.dto.response.*;
import com.fastned.solarcharging.service.NetworkService;
import com.fastned.solarcharging.service.SolarGridService;
import com.fastned.solarcharging.service.UserService;
import com.fastned.solarcharging.service.util.SolarGridUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;

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

    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @PostMapping("/load")
    public ResponseEntity<ApiResponse<String>>  handleSolarGridStateFileUpload(@RequestBody List<SolarGridRequest> solarGrid,
                                                                               Authentication auth,
                                                                               RedirectAttributes redirectAttributes)  {


        UserDetails user = (UserDetails)auth.getPrincipal();

        UserResponse userResponse = userService.findByName(user.getUsername());

        NetworkCreateResponse response = SolarGridUtils.processIncomingNetworkFile2(networkService, solarGridService, solarGrid, userResponse, null);

        return ResponseEntity.status(HttpStatus.RESET_CONTENT).body(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response.toString()));
    }


    /**
     * Fetches all Solar Grid based on the given userId
     *
     * @param userId
     * @return List of NetworkResponse
     */
    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @GetMapping("/output/{totalDays}")
    public ResponseEntity<ApiResponse<List<NetworkResponse>>> findAllByUserId(@PathVariable long userId) {
        final List<NetworkResponse> response = networkService.findAllByUserId(userId);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }


}
