package com.fastned.solarcharging.service;

import com.fastned.solarcharging.dto.mapper.SolarGridRequestMapper;
import com.fastned.solarcharging.dto.mapper.SolarGridResponseMapper;
import com.fastned.solarcharging.dto.request.SolarGridRequest;
import com.fastned.solarcharging.exception.ElementAlreadyExistsException;
import com.fastned.solarcharging.exception.NoSuchElementFoundException;
import com.fastned.solarcharging.model.SolarGrid;
import com.fastned.solarcharging.repository.SolarGridRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit Test for SolarGridService methods
 */
@ExtendWith(MockitoExtension.class)
class SolarGridServiceTest {

    @InjectMocks
    private SolarGridService solarGridService;

    @Mock
    private SolarGridRepository solarGridRepository;

    @Mock
    private SolarGridRequestMapper solarGridRequestMapper;

    @Mock
    private SolarGridResponseMapper solarGridResponseMapper;

    @Captor
    private ArgumentCaptor<SolarGrid> solarGridCaptor;

    /**
     * Method under test: {@link SolarGridService#getById(long)}
     */
    @Test
    void getById_should_throwNoSuchElementFoundException_when_IsNotFound() {
        long id = 101L;
        when(solarGridRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> {
            solarGridService.getById(id);
        });

        verify(solarGridRepository).findById(id);
    }


    /**
     * Method under test: {@link SolarGridService#findAll(Pageable)}
     */
    @Test
    void findAll_should_throwNoSuchElementFoundException_when_IsNotFound() {
        Pageable pageable = PageRequest.of(0, 10);
        when(solarGridRepository.findAll(pageable)).thenReturn(new PageImpl<>(new ArrayList<>()));

        assertThrows(NoSuchElementFoundException.class, () -> {
            solarGridService.findAll(pageable);
        });

        verify(solarGridRepository).findAll(pageable);
    }


    /**
     * Method under test: {@link SolarGridService#create(SolarGridRequest)}
     */
    @Test
    void create_should_throwElementAlreadyExistsException_when_SolarGridAlreadyExists() {
        SolarGridRequest request = new SolarGridRequest();
        request.setName("Amsterdam");

        when(solarGridRepository.existsByNameIgnoreCase("Amsterdam")).thenReturn(true);

        assertThrows(ElementAlreadyExistsException.class, () -> {
            solarGridService.create(request);
        });

        verify(solarGridRepository, never()).save(any());
    }


    /**
     * Method under test: {@link SolarGridService#update(SolarGridRequest)}
     */
    @Test
    void update_should_throwElementAlreadyExistsException_when_SolarGridAlreadyExists() {
        SolarGrid solarGrid = new SolarGrid();
        solarGrid.setId(101L);
        solarGrid.setName("Maastricht");

        SolarGridRequest request = new SolarGridRequest();
        request.setId(101L);
        request.setName("Amsterdam");

        when(solarGridRepository.findById(101L)).thenReturn(Optional.of(solarGrid));
        when(solarGridRepository.existsByNameIgnoreCase("Amsterdam")).thenReturn(true);

        assertThrows(ElementAlreadyExistsException.class, () -> {
            solarGridService.update(request);
        });

        verify(solarGridRepository, never()).save(any());
    }

    /**
     * Method under test: {@link SolarGridService#deleteById(long)}
     */
    @Test
    void deleteById_should_deleteSolarGrids_when_IsFound() {
        SolarGrid solarGrid = new SolarGrid();
        solarGrid.setId(101L);
        solarGrid.setName("Maastricht");

        when(solarGridRepository.findById(101L)).thenReturn(Optional.of(solarGrid));

        solarGridService.deleteById(101L);
        verify(solarGridRepository).delete(solarGridCaptor.capture());
        SolarGrid capturedSolarGrid = solarGridCaptor.getValue();

        assertEquals(101L, capturedSolarGrid.getId());
        assertEquals("Maastricht", capturedSolarGrid.getName());
    }

    /**
     * Method under test: {@link SolarGridService#deleteById(long)}
     */
    @Test
    void deleteById_should_throwNoSuchElementFoundException_when_SolarGridsIsNotFound() {
        long id = 101L;
        when(solarGridRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> {
            solarGridService.deleteById(id);
        });

        verify(solarGridRepository).findById(id);
        verify(solarGridRepository, never()).delete(any());
    }
}
