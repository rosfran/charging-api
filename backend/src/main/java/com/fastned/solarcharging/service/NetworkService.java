package com.fastned.solarcharging.service;

import com.fastned.solarcharging.common.Constants;
import com.fastned.solarcharging.dto.request.NetworkRequest;
import com.fastned.solarcharging.dto.response.NetworkResponse;
import com.fastned.solarcharging.exception.NoSuchElementFoundException;
import com.fastned.solarcharging.model.Network;
import com.fastned.solarcharging.repository.NetworkRepository;
import com.fastned.solarcharging.dto.mapper.NetworkRequestMapper;
import com.fastned.solarcharging.dto.mapper.NetworkResponseMapper;
import com.fastned.solarcharging.dto.response.CommandResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service used for Network related operations
 *
 * Each solar grid produces an optimal power output of 20*(1-(D/365*0,005))kW (D=age in days).
 * Each year has 1550 hours of sun.
 * Since NL is not a very sunny country, we can (only) expect around 2,5 kWh per m2
 * To compensate days with a lower sun strength, we simplify our model to 1000 hours of “full sun”
 * Power output can be calculated over the “full sun” hours with the expectation of optimal power output of solar panels during those “full sun” hours.
 * Solar grids have to be installed first, so only start to produce energy after 60 days of placement (age 0 is day of placement).
 * A solar panel breaks down after 25 years of usage.
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
     * Fetches a single network by the given id
     *
     * @param id
     * @return NetworkResponse
     */
    public NetworkResponse findById(long id) {
        return networkRepository.findById(id)
                .map(networkResponseMapper::toDto)
                .orElseThrow(() -> new NoSuchElementFoundException(Constants.NOT_FOUND_NETWORK));
    }

    /**
     * Fetches all networks based on the given paging and sorting parameters
     *
     * @param pageable
     * @return List of NetworkResponse
     */
    @Transactional(readOnly = true)
    public Page<NetworkResponse> findAll(Pageable pageable) {
        final Page<NetworkResponse> networks = networkRepository.findAll(pageable)
                .map(networkResponseMapper::toDto);

        if (networks.isEmpty())
            throw new NoSuchElementFoundException(Constants.NOT_FOUND_RECORD);
        return networks;
    }

    /**
     * Fetches all networks based on the given userId
     *
     * @param userId
     * @return List of NetworkResponse
     */
    @Transactional(readOnly = true)
    public List<NetworkResponse> findAllByUserId(long userId) {
        final List<NetworkResponse> networks = networkRepository.findAllByUserId(userId).stream()
                .map(networkResponseMapper::toDto).toList();

        if (networks.isEmpty())
            throw new NoSuchElementFoundException(Constants.NOT_FOUND_RECORD);
        return networks;
    }


    /**
     * @param request
     * @return id of the created network
     */
    public CommandResponse create(NetworkRequest request) {
        final Network network = networkRequestMapper.toEntity(request);
        network.setUser(userService.getById(request.getIdUser()));
        networkRepository.save(network);
        log.info(Constants.CREATED_NETWORK);
        return CommandResponse.builder().id(network.getId()).build();
    }


    /**
     * Updates network using the given request parameters
     *
     * @param request
     * @return id of the updated network
     */
    public CommandResponse update(NetworkRequest request) {
        if (!networkRepository.existsById(request.getId()))
            throw new NoSuchElementFoundException(Constants.NOT_FOUND_NETWORK);

        final Network network = networkRequestMapper.toEntity(request);
        network.setUser(userService.getById(request.getIdUser()));
        networkRepository.save(network);
        log.info(Constants.UPDATED_NETWORK);
        return CommandResponse.builder().id(network.getId()).build();
    }

    /**
     * Deletes network by the given id
     *
     * @param id
     */
    public void deleteById(long id) {
        final Network network = networkRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(Constants.NOT_FOUND_NETWORK));
        networkRepository.delete(network);
        log.info(Constants.DELETED_NETWORK);
    }
}
