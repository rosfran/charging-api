package com.fastned.solarcharging.controller;

import com.fastned.solarcharging.dto.request.SolarGridRequest;
import com.fastned.solarcharging.dto.response.*;
import com.fastned.solarcharging.service.NetworkService;
import com.fastned.solarcharging.service.SolarGridService;
import com.fastned.solarcharging.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

import static com.fastned.solarcharging.common.Constants.SUCCESS;

/**
 * A SolarGrid is the minimum unit for producing energy. It needs to be
 * installed for at least 60 days before starting to produce energy
 *
 * This class implements some endpoints to Create, Update and Delete SolarGrids
 */
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j(topic = "SolarGridController")
@RestController
@RequestMapping("/api/v1/solar-grid")
@RequiredArgsConstructor
public class SolarGridController {

    private final Clock clock;
    private final NetworkService networkService;

    private final SolarGridService solarGridService;
    private final UserService userService;

    /**
     * Fetches a single SolarGrid by the given id
     *
     * @param id            The SolarGrid ID
     * @return SolarGridResponse
     */
    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SolarGridResponse>> findById(@PathVariable long id) {
        final SolarGridResponse response = solarGridService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>( SUCCESS, response));
    }

    /**
     * Fetches a single SolarGrid by the given User id
     *
     * @param id                    The SolarGrid ID
     * @return SolarGridResponse
     */
    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<List<SolarGridResponse>>> findByUserId(@PathVariable long id) {
        final List<SolarGridResponse> response = solarGridService.findByUserId(id);
        return ResponseEntity.ok(new ApiResponse<>( SUCCESS, response));
    }

    /**
     * Fetches all SolarGrids based on the given parameters
     *
     * @param pageable
     * @return List of SolarGridResponse
     */
    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<SolarGridResponse>>> findAll(Pageable pageable) {
        final Page<SolarGridResponse> response = solarGridService.findAll(pageable);
        return ResponseEntity.ok(new ApiResponse<>( SUCCESS, response));
    }

    /**
     * Creates a new SolarGrids using the given request parameters
     *
     * @param request
     * @return id of the created type
     */
    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @PostMapping
    public ResponseEntity<ApiResponse<SolarGridResponse>> create(@Valid @RequestBody SolarGridRequest request) {
        final SolarGridResponse response = solarGridService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>( SUCCESS, response));
    }

    /**
     * Updates a given SolarGrid using the given request parameters
     *
     * @return id of the updated type
     */
    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @PutMapping
    public ResponseEntity<ApiResponse<CommandResponse>> update(@Valid @RequestBody SolarGridRequest request) {
        final CommandResponse response = solarGridService.update(request);
        return ResponseEntity.ok(new ApiResponse<>( SUCCESS, response));
    }

    /**
     * Deletes SolarGrid by id
     *
     * @param id
     */
    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable long id) {
        solarGridService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
