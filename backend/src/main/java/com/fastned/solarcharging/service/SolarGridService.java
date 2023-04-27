package com.fastned.solarcharging.service;

import com.fastned.solarcharging.common.Constants;
import com.fastned.solarcharging.dto.request.SolarGridRequest;
import com.fastned.solarcharging.dto.response.SolarGridResponse;
import com.fastned.solarcharging.exception.ElementAlreadyExistsException;
import com.fastned.solarcharging.exception.NoSuchElementFoundException;
import com.fastned.solarcharging.repository.SolarGridRepository;
import com.fastned.solarcharging.dto.mapper.SolarGridRequestMapper;
import com.fastned.solarcharging.dto.mapper.SolarGridResponseMapper;
import com.fastned.solarcharging.dto.response.CommandResponse;
import com.fastned.solarcharging.model.SolarGrid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service used for SolarGrid related operations
 */
@Slf4j(topic = "SolarGridService")
@Service
@RequiredArgsConstructor
public class SolarGridService {

    private final SolarGridRepository solarGridRepository;
    private final SolarGridRequestMapper solarGridRequestMapper;
    private final SolarGridResponseMapper solarGridResponseMapper;

    /**
     * Fetches a single type by the given id
     *
     * @param id
     * @return SolarGridResponse
     */
    public SolarGridResponse findById(long id) {
        return solarGridRepository.findById(id)
                .map(solarGridResponseMapper::toDto)
                .orElseThrow(() -> new NoSuchElementFoundException(Constants.NOT_FOUND_TYPE));
    }

    /**
     * Fetches a single type (entity) by the given id
     *
     * @param id
     * @return SolarGrid
     */
    public SolarGrid getById(long id) {
        return solarGridRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(Constants.NOT_FOUND_TYPE));
    }

    /**
     * Fetches all types based on the given paging and sorting parameters
     *
     * @param pageable
     * @return List of SolarGridResponse
     */
    @Transactional(readOnly = true)
    public Page<SolarGridResponse> findAll(Pageable pageable) {
        final Page<SolarGridResponse> types = solarGridRepository.findAll(pageable)
                .map(solarGridResponseMapper::toDto);

        if (types.isEmpty())
            throw new NoSuchElementFoundException(Constants.NOT_FOUND_RECORD);
        return types;
    }

    /**
     * Creates a new type using the given request parameters
     *
     * @param request
     * @return id of the created type
     */
    public CommandResponse create(SolarGridRequest request) {
        if (solarGridRepository.existsByNameIgnoreCase(request.getName()))
            throw new ElementAlreadyExistsException(Constants.ALREADY_EXISTS_TYPE);

        final SolarGrid solarGrid = solarGridRequestMapper.toEntity(request);
        solarGridRepository.save(solarGrid);
        log.info(Constants.CREATED_TYPE);
        return CommandResponse.builder().id(solarGrid.getId()).build();
    }

    /**
     * Updates type using the given request parameters
     *
     * @param request
     * @return id of the updated type
     */
    public CommandResponse update(SolarGridRequest request) {
        final SolarGrid solarGrid = solarGridRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementFoundException(Constants.NOT_FOUND_TYPE));

        // if the name value of the request is different, check if a record with this name already exists
        if (!request.getName().equalsIgnoreCase(solarGrid.getName()) && solarGridRepository.existsByNameIgnoreCase(request.getName()))
            throw new ElementAlreadyExistsException(Constants.ALREADY_EXISTS_TYPE);

        solarGridRepository.save(solarGridRequestMapper.toEntity(request));
        log.info(Constants.UPDATED_TYPE);
        return CommandResponse.builder().id(solarGrid.getId()).build();
    }

    /**
     * Deletes type by the given id
     *
     * @param id
     */
    public void deleteById(long id) {
        final SolarGrid solarGrid = solarGridRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementFoundException(Constants.NOT_FOUND_TYPE));
        solarGridRepository.delete(solarGrid);
        log.info(Constants.DELETED_TYPE);
    }
}
