package com.fastned.solarcharging.service;

import com.fastned.solarcharging.common.Constants;
import com.fastned.solarcharging.dto.request.NetworkRequest;
import com.fastned.solarcharging.dto.response.NetworkResponse;
import com.fastned.solarcharging.exception.NoSuchElementFoundException;
import com.fastned.solarcharging.model.Network;
import com.fastned.solarcharging.repository.NetworkRepository;
import com.fastned.solarcharging.dto.mapper.NetworkRequestMapper;
import com.fastned.solarcharging.dto.mapper.NetworkResponseMapper;
import com.fastned.solarcharging.dto.request.TypeSetRequest;
import com.fastned.solarcharging.dto.response.CommandResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service used for Network related operations
 */
@Slf4j(topic = "NetworkService")
@Service
@RequiredArgsConstructor
public class NetworkService {

    private final NetworkRepository networkRepository;
    private final SolarGridService solarGridService;
    private final UserService userService;
    private final NetworkRequestMapper networkRequestMapper;
    private final NetworkResponseMapper networkResponseMapper;

    /**
     * Fetches a single pet by the given id
     *
     * @param id
     * @return NetworkResponse
     */
    public NetworkResponse findById(long id) {
        return networkRepository.findById(id)
                .map(networkResponseMapper::toDto)
                .orElseThrow(() -> new NoSuchElementFoundException(Constants.NOT_FOUND_PET));
    }

    /**
     * Fetches all pets based on the given paging and sorting parameters
     *
     * @param pageable
     * @return List of NetworkResponse
     */
    @Transactional(readOnly = true)
    public Page<NetworkResponse> findAll(Pageable pageable) {
        final Page<NetworkResponse> pets = networkRepository.findAll(pageable)
                .map(networkResponseMapper::toDto);

        if (pets.isEmpty())
            throw new NoSuchElementFoundException(Constants.NOT_FOUND_RECORD);
        return pets;
    }

    /**
     * Fetches all pets based on the given userId
     *
     * @param userId
     * @return List of NetworkResponse
     */
    @Transactional(readOnly = true)
    public List<NetworkResponse> findAllByUserId(long userId) {
        final List<NetworkResponse> pets = networkRepository.findAllByUserId(userId).stream()
                .map(networkResponseMapper::toDto).toList();

        if (pets.isEmpty())
            throw new NoSuchElementFoundException(Constants.NOT_FOUND_RECORD);
        return pets;
    }

    /**
     * Fetches counts of all pets by selected type
     *
     * @param types
     * @return selected type names and count of each type
     */
    @Transactional(readOnly = true)
    public Map<String, Long> findAllByType(TypeSetRequest types) {
        return networkRepository.findAll().stream()
                .filter(pet -> types.getIds().isEmpty() || types.getIds().contains(pet.getType().getId()))
                .collect(Collectors.groupingBy(x -> x.getType().getName(), Collectors.counting()));
        // if we need to return SolarGridResponse instead of String (type names), we can use this:
        // .collect(Collectors.groupingBy(x -> typeResponseMapper.toDto(x.getType()), Collectors.counting()));
    }

    /**
     * Creates a new pet using the given request parameters
     *
     * @param request
     * @return id of the created pet
     */
    public CommandResponse create(NetworkRequest request) {
        final Network network = networkRequestMapper.toEntity(request);
        network.setType(solarGridService.getById(request.getTypeId()));
        network.setUser(userService.getById(request.getIdUser()));
        networkRepository.save(network);
        log.info(Constants.CREATED_PET);
        return CommandResponse.builder().id(network.getId()).build();
    }

    /**
     * Updates pet using the given request parameters
     *
     * @param request
     * @return id of the updated pet
     */
    public CommandResponse update(NetworkRequest request) {
        if (!networkRepository.existsById(request.getId()))
            throw new NoSuchElementFoundException(Constants.NOT_FOUND_PET);

        final Network network = networkRequestMapper.toEntity(request);
        network.setType(solarGridService.getById(request.getTypeId()));
        network.setUser(userService.getById(request.getIdUser()));
        networkRepository.save(network);
        log.info(Constants.UPDATED_PET);
        return CommandResponse.builder().id(network.getId()).build();
    }

    /**
     * Deletes pet by the given id
     *
     * @param id
     */
    public void deleteById(long id) {
        final Network network = networkRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(Constants.NOT_FOUND_PET));
        networkRepository.delete(network);
        log.info(Constants.DELETED_PET);
    }
}
