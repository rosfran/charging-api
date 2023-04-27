package com.fastned.solarcharging.controller;

import com.fastned.solarcharging.dto.request.SolarGridRequest;
import com.fastned.solarcharging.dto.response.ApiResponse;
import com.fastned.solarcharging.dto.response.CommandResponse;
import com.fastned.solarcharging.dto.response.SolarGridResponse;
import com.fastned.solarcharging.service.SolarGridService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.Instant;

import static com.fastned.solarcharging.common.Constants.SUCCESS;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/v1/types")
@RequiredArgsConstructor
public class SolarGridController {

    private final Clock clock;
    private final SolarGridService solarGridService;

    /**
     * Fetches a single type by the given id
     *
     * @param id
     * @return SolarGridResponse
     */
    @PreAuthorize("hasRole(T(com.fastned.fastcharging.model.RoleType).ROLE_USER)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SolarGridResponse>> findById(@PathVariable long id) {
        final SolarGridResponse response = solarGridService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Fetches all types based on the given parameters
     *
     * @param pageable
     * @return List of SolarGridResponse
     */
    @PreAuthorize("hasRole(T(com.fastned.fastcharging.model.RoleType).ROLE_USER)")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<SolarGridResponse>>> findAll(Pageable pageable) {
        final Page<SolarGridResponse> response = solarGridService.findAll(pageable);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Creates a new type using the given request parameters
     *
     * @param request
     * @return id of the created type
     */
    @PreAuthorize("hasRole(T(com.fastned.fastcharging.model.RoleType).ROLE_USER)")
    @PostMapping
    public ResponseEntity<ApiResponse<CommandResponse>> create(@Valid @RequestBody SolarGridRequest request) {
        final CommandResponse response = solarGridService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Updates type using the given request parameters
     *
     * @return id of the updated type
     */
    @PreAuthorize("hasRole(T(com.fastned.fastcharging.model.RoleType).ROLE_USER)")
    @PutMapping
    public ResponseEntity<ApiResponse<CommandResponse>> update(@Valid @RequestBody SolarGridRequest request) {
        final CommandResponse response = solarGridService.update(request);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Deletes type by id
     *
     * @param id
     */
    @PreAuthorize("hasRole(T(com.fastned.fastcharging.model.RoleType).ROLE_USER)")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable long id) {
        solarGridService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}