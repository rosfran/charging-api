package com.fastned.solarcharging.controller;

import com.fastned.solarcharging.dto.request.LoadFileRequest;
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

@Slf4j(topic = "NetworkController")
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/network")
@RequiredArgsConstructor
public class NetworkController {

    private final Clock clock;
    private final NetworkService networkService;

    private final SolarGridService solarGridService;
    private final UserService userService;

    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @PostMapping("/load-file")
    public ResponseEntity<ApiResponse<String>>  handleSolarGridStateFileUpload(@RequestPart("file") MultipartFile file,
                                                                               @RequestPart("body") LoadFileRequest body,
                                                                               Authentication auth,
                                                                               RedirectAttributes redirectAttributes)  {
        final JsonParser springParser = JsonParserFactory.getJsonParser();

        String fileContent = "";

        try {
            fileContent = new String( file.getInputStream().readAllBytes() );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Object> list = springParser.parseList(fileContent);

        UserDetails user = (UserDetails)auth.getPrincipal();

        UserResponse userResponse = userService.findByName(user.getUsername());

        NetworkCreateResponse response = SolarGridUtils.processIncomingNetworkFile(networkService, solarGridService, list, userResponse, body.getTimeElapsedDays());

        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return ResponseEntity.ok(new ApiResponse<>( SUCCESS, response.toString()));
    }


    /**
     * Fetches a single solar grid by the given id
     *
     * @param id
     * @return NetworkResponse
     */
    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NetworkResponse>> findById(@PathVariable long id) {
        final NetworkResponse response = networkService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>( SUCCESS, response));
    }

    /**
     * Fetches all Solar Grid based on the given userId
     *
     * @param userId
     * @return List of NetworkResponse
     */
    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<List<NetworkResponse>>> findAllByUserId(@PathVariable long userId) {
        final List<NetworkResponse> response = networkService.findAllByUserId(userId);
        return ResponseEntity.ok(new ApiResponse<>( SUCCESS, response));
    }


    /**
     * Fetches all Solar Grids based on the given parameters
     *
     * @param pageable
     * @return List of NetworkResponse
     */
    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<NetworkResponse>>> findAll(Pageable pageable) {
        final Page<NetworkResponse> response = networkService.findAll(pageable);
        return ResponseEntity.ok(new ApiResponse<>( SUCCESS, response));
    }

    /**
     * Creates a new solar grid using the given request parameters
     *
     * @param request
     * @return id of the created solar grid
     */
    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @PostMapping
    public ResponseEntity<ApiResponse<CommandResponse>> create(@Valid @RequestBody NetworkRequest request) {
        final CommandResponse response = networkService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>( SUCCESS, response));
    }

    /**
     * Updates solar grid using the given request parameters
     *
     * @return id of the updated solar grid
     */
    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @PutMapping
    public ResponseEntity<ApiResponse<CommandResponse>> update(@Valid @RequestBody NetworkRequest request) {
        final CommandResponse response = networkService.update(request);
        return ResponseEntity.ok(new ApiResponse<>( SUCCESS, response));
    }

    /**
     * Deletes solar grid by id
     *
     * @param id
     */
    @PreAuthorize("hasRole(T(com.fastned.solarcharging.model.RoleType).ROLE_USER)")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteById(@PathVariable long id) {
        networkService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
