package com.fastned.solarcharging.controller;

import com.fastned.solarcharging.dto.request.SolarGridRequest;
import com.fastned.solarcharging.dto.request.StateRequest;
import com.fastned.solarcharging.dto.response.ApiResponse;
import com.fastned.solarcharging.dto.response.CommandResponse;
import com.fastned.solarcharging.dto.response.SolarGridResponse;
import com.fastned.solarcharging.dto.response.StateResponse;
import com.fastned.solarcharging.service.StateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.fastned.solarcharging.common.Constants.SUCCESS;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/v1/state")
@RequiredArgsConstructor
public class StateController {

    private final Clock clock;
    private final StateService stateService;

    /**
     * Fetches a single State by the given id
     *
     * @param id
     * @return StateResponse
     */
    @PreAuthorize("hasRole(T(com.fastned.fastcharging.model.RoleType).ROLE_USER)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StateResponse>> findById(@PathVariable long id) {
        final StateResponse response = stateService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Fetches all SolarGrids based on the given parameters
     *
     * @param pageable
     * @return List of SolarGridResponse
     */
    @PreAuthorize("hasRole(T(com.fastned.fastcharging.model.RoleType).ROLE_USER)")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<StateResponse>>> findAll(Pageable pageable) {
        final Page<StateResponse> response = stateService.findAll(pageable);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Creates a new SolarGrids using the given request parameters
     *
     * @param request
     * @return id of the created type
     */
    @PreAuthorize("hasRole(T(com.fastned.fastcharging.model.RoleType).ROLE_USER)")
    @PostMapping
    public ResponseEntity<ApiResponse<CommandResponse>> create(@Valid @RequestBody StateRequest request) {
        final CommandResponse response = stateService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Updates a given State using the given request parameters
     *
     * @return id of the updated type
     */
    @PreAuthorize("hasRole(T(com.fastned.fastcharging.model.RoleType).ROLE_USER)")
    @PutMapping
    public ResponseEntity<ApiResponse<CommandResponse>> update(@Valid @RequestBody StateRequest request) {
        final CommandResponse response = stateService.update(request);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Deletes State by id
     *
     * @param id
     */
    @PreAuthorize("hasRole(T(com.fastned.fastcharging.model.RoleType).ROLE_USER)")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable long id) {
        stateService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
