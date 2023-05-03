package com.fastned.solarcharging.service;

import com.fastned.solarcharging.common.Constants;
import com.fastned.solarcharging.dto.mapper.SolarGridRequestMapper;
import com.fastned.solarcharging.dto.mapper.SolarGridResponseMapper;
import com.fastned.solarcharging.dto.mapper.StateRequestMapper;
import com.fastned.solarcharging.dto.mapper.StateResponseMapper;
import com.fastned.solarcharging.dto.request.SolarGridRequest;
import com.fastned.solarcharging.dto.request.StateRequest;
import com.fastned.solarcharging.dto.response.CommandResponse;
import com.fastned.solarcharging.dto.response.SolarGridResponse;
import com.fastned.solarcharging.dto.response.StateResponse;
import com.fastned.solarcharging.exception.ElementAlreadyExistsException;
import com.fastned.solarcharging.exception.NoSuchElementFoundException;
import com.fastned.solarcharging.model.SolarGrid;
import com.fastned.solarcharging.model.State;
import com.fastned.solarcharging.repository.NetworkRepository;
import com.fastned.solarcharging.repository.SolarGridRepository;
import com.fastned.solarcharging.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service used for SolarGrid related operations
 */
@Slf4j(topic = "StateService")
@Service
@RequiredArgsConstructor
public class StateService {

    private final SolarGridRepository solarGridRepository;
    private final StateRepository stateRepository;

    private final NetworkRepository networkRepository;
    private final SolarGridRequestMapper solarGridRequestMapper;
    private final StateRequestMapper stateRequestMapper;
    private final StateResponseMapper stateResponseMapper;

    /**
     * Fetches a single solar grid by the given id
     *
     * @param id
     * @return SolarGridResponse
     */
    public StateResponse findById(long id) {
        return stateRepository.findById(id)
                .map(stateResponseMapper::toDto)
                .orElseThrow(() -> new NoSuchElementFoundException(Constants.NOT_FOUND_SOLAR_GRID));
    }

    /**
     * Fetches a single solar grid (entity) by the given id
     *
     * @param id
     * @return SolarGrid
     */
    public SolarGrid getById(long id) {
        return solarGridRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(Constants.NOT_FOUND_SOLAR_GRID));
    }

    /**
     * Fetches all solar grids based on the given paging and sorting parameters
     *
     * @param pageable
     * @return List of SolarGridResponse
     */
    @Transactional(readOnly = true)
    public Page<StateResponse> findAll(Pageable pageable) {
        final Page<StateResponse> solargrids = stateRepository.findAll(pageable)
                .map(stateResponseMapper::toDto);

        if (solargrids.isEmpty())
            throw new NoSuchElementFoundException(Constants.NOT_FOUND_RECORD);
        return solargrids;
    }

    /**
     * Creates a new solar grid using the given request parameters
     *
     * @param request
     * @return id of the created solar grid
     */
    public CommandResponse create(StateRequest request) {
        final State state = stateRequestMapper.toEntity(request);
        stateRepository.save(state);
        log.info(Constants.CREATED_SOLAR_GRID);
        return CommandResponse.builder().id(state.getId()).build();
    }

    /**
     * Updates solar grid using the given request parameters
     *
     * @param request
     * @return id of the updated solar grid
     */
    public CommandResponse update(StateRequest request) {
        final State state = stateRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementFoundException(Constants.NOT_FOUND_SOLAR_GRID));

        stateRepository.save(stateRequestMapper.toEntity(request));
        log.info(Constants.UPDATED_SOLAR_GRID);
        return CommandResponse.builder().id(state.getId()).build();
    }

    /**
     * Deletes solar grid by the given id
     *
     * @param id
     */
    public void deleteById(long id) {
        final SolarGrid solarGrid = solarGridRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(Constants.NOT_FOUND_SOLAR_GRID));
        solarGridRepository.delete(solarGrid);
        log.info(Constants.DELETED_SOLAR_GRID);
    }
}
