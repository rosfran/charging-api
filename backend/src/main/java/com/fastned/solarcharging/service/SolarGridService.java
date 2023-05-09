package com.fastned.solarcharging.service;

import com.fastned.solarcharging.common.Constants;
import com.fastned.solarcharging.dto.mapper.NetworkRequestMapper;
import com.fastned.solarcharging.dto.request.SolarGridRequest;
import com.fastned.solarcharging.dto.response.NetworkResponse;
import com.fastned.solarcharging.dto.response.SolarGridResponse;
import com.fastned.solarcharging.exception.ElementAlreadyExistsException;
import com.fastned.solarcharging.exception.NoSuchElementFoundException;
import com.fastned.solarcharging.model.Network;
import com.fastned.solarcharging.repository.SolarGridRepository;
import com.fastned.solarcharging.repository.NetworkRepository;
import com.fastned.solarcharging.dto.mapper.SolarGridRequestMapper;
import com.fastned.solarcharging.dto.mapper.SolarGridResponseMapper;
import com.fastned.solarcharging.dto.response.CommandResponse;
import com.fastned.solarcharging.model.SolarGrid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.fastned.solarcharging.service.util.SolarGridUtils.calculatePowerOutput;

/**
 * Service used for SolarGrid related operations
 *
 * A SolarGrid is the minimum unit used for producing energy. It must be part of a Network, in a sense that, even when
 * we have only one SolarGrid, this unique SolarGrid must be in a Network
 *
 */
@Slf4j(topic = "SolarGridService")
@Service
@RequiredArgsConstructor
public class SolarGridService {

    private final SolarGridRepository solarGridRepository;
    private final NetworkRepository networkRepository;
    private final SolarGridRequestMapper solarGridRequestMapper;
    private final SolarGridResponseMapper solarGridResponseMapper;
    private final NetworkRequestMapper networkRequestMapper;

    /**
     * Fetches a single solar grid by the given id
     *
     * @param id
     * @return SolarGridResponse
     */
    public SolarGridResponse findById(long id) {
        return solarGridRepository.findById(id)
                .map(solarGridResponseMapper::toDto)
                .orElseThrow(() -> new NoSuchElementFoundException(Constants.NOT_FOUND_SOLAR_GRID));
    }

    /**
     * Fetches a single solar grid by the given id
     *
     * @param userId
     * @return SolarGridResponse
     */
    public List<SolarGridResponse> findByUserId(long userId) {
        List<Network> lstNetworks = networkRepository.findAllByUserId(userId);

        if ( lstNetworks.size() == 0 ) {
            throw new  NoSuchElementFoundException(Constants.NOT_FOUND_NETWORK);
        }

        List<SolarGridResponse> resp = new ArrayList<>();

        // iterates over all networks, and push all SolarGridResponse for each Network
        for (final Network n : lstNetworks ) {
            resp.addAll(solarGridRepository.findAllByNetworkId(n.getId())
                    .stream().map(solarGridResponseMapper::toDto).toList());
        }

        return resp;
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
    public Page<SolarGridResponse> findAll(Pageable pageable) {
        final Page<SolarGridResponse> solargrids = solarGridRepository.findAll(pageable)
                .map(solarGridResponseMapper::toDto);

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
    public SolarGridResponse create(SolarGridRequest request) {
        if (solarGridRepository.existsByNameIgnoreCase(request.getName()))
            throw new ElementAlreadyExistsException(Constants.ALREADY_EXISTS_SOLAR_GRID);

        final SolarGrid solarGrid = solarGridRequestMapper.toEntity(request);

        solarGrid.setPowerOutput(calculatePowerOutput(request.getAge()));
        Optional<Network> net = networkRepository.findById(request.getIdNetwork());

        solarGrid.setNetwork(net.get());
        solarGridRepository.save(solarGrid);
        log.info(Constants.CREATED_SOLAR_GRID);
        return solarGridResponseMapper.toDto(solarGrid);
    }

    /**
     * Updates solar grid using the given request parameters
     *
     * @param request
     * @return id of the updated solar grid
     */
    public CommandResponse update(SolarGridRequest request) {
        final SolarGrid solarGrid = solarGridRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementFoundException(Constants.NOT_FOUND_SOLAR_GRID));

        // if the name value of the request is different, check if a record with this name already exists
        if (!request.getName().equalsIgnoreCase(solarGrid.getName()) && solarGridRepository.existsByNameIgnoreCase(request.getName()))
            throw new ElementAlreadyExistsException(Constants.ALREADY_EXISTS_SOLAR_GRID);

        solarGrid.setPowerOutput(calculatePowerOutput(request.getAge()));

        solarGridRepository.save(solarGrid);
        log.info(Constants.UPDATED_SOLAR_GRID);
        return CommandResponse.builder().id(solarGrid.getId()).build();
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
