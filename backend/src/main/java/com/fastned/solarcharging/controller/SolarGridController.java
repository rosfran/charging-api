package com.fastned.solarcharging.controller;

import com.fastned.solarcharging.dto.request.SolarGridRequest;
import com.fastned.solarcharging.dto.request.StateRequest;
import com.fastned.solarcharging.dto.response.*;
import com.fastned.solarcharging.service.SolarGridService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.fastned.solarcharging.common.Constants.SUCCESS;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/v1/solar-grid")
@RequiredArgsConstructor
public class SolarGridController {

    private final Clock clock;
    private final SolarGridService solarGridService;
    private final StateService stateService;


    //@PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @PostMapping("/receive-file")
    public ResponseEntity<ApiResponse<SolarGridCreateResponse>>  handleSolarGridStateFileUpload(@RequestParam("file") MultipartFile file,
                                                 Authentication auth,
                                                 RedirectAttributes redirectAttributes)  {

        JsonParser springParser = JsonParserFactory.getJsonParser();

        String fileContent = "";

        try {
            fileContent = new String( file.getInputStream().readAllBytes() );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Object> list = springParser.parseList(fileContent);

        UserDetails user = (UserDetails)auth.getPrincipal();
        List<StateRequest> states = new ArrayList<>();
        SolarGridCreateResponse response = new SolarGridCreateResponse();
        for (Object o: list) {
            if (o instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) o;
                System.out.println("Items found: " + map.size());
                int i = 0;

                SolarGridRequest solRequest = new SolarGridRequest();

                StateRequest req = new StateRequest();
                for (Map.Entry<String,Object> entry: map.entrySet()) {
                    System.out.println(entry.getKey() + " = " + entry.getValue());

                    if ( entry.getKey().equalsIgnoreCase("name") ) {
                        solRequest.setName((String) entry.getValue());
                    } else if ( entry.getKey().equalsIgnoreCase("age")) {
                        int value = -1;
                        value = (Integer)entry.getValue();
                        req.setAge(value);
                    }

                    i++;
                }

                CommandResponse id = solarGridService.create(solRequest);

                req.setIdSolarGrid(id.id());
                stateService.create(req);

                response.getResponseList().add( new StateResponse( req.getAge(), req.getName() ));
            }
        }

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Fetches a single SolarGrid by the given id
     *
     * @param id
     * @return SolarGridResponse
     */
    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SolarGridResponse>> findById(@PathVariable long id) {
        final SolarGridResponse response = solarGridService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Fetches a single SolarGrid by the given User id
     *
     * @param id
     * @return SolarGridResponse
     */
    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse<Page<SolarGridResponse>>> findByUserId(@PathVariable long id) {
        final Page<SolarGridResponse> response = solarGridService.findByUserId(id);
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
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
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
    }

    /**
     * Creates a new SolarGrids using the given request parameters
     *
     * @param request
     * @return id of the created type
     */
    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @PostMapping
    public ResponseEntity<ApiResponse<CommandResponse>> create(@Valid @RequestBody SolarGridRequest request) {
        final CommandResponse response = solarGridService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
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
        return ResponseEntity.ok(new ApiResponse<>(Instant.now(clock).toEpochMilli(), SUCCESS, response));
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
