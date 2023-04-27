package com.fastned.solarcharging.service;

import com.fastned.solarcharging.dto.mapper.SolarGridRequestMapper;
import com.fastned.solarcharging.dto.mapper.SolarGridResponseMapper;
import com.fastned.solarcharging.dto.request.SolarGridRequest;
import com.fastned.solarcharging.dto.response.CommandResponse;
import com.fastned.solarcharging.dto.response.SolarGridResponse;
import com.fastned.solarcharging.exception.ElementAlreadyExistsException;
import com.fastned.solarcharging.exception.NoSuchElementFoundException;
import com.fastned.solarcharging.model.SolarGrid;
import com.fastned.solarcharging.repository.SolarGridRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
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
    private ArgumentCaptor<SolarGrid> typeCaptor;

    /**
     * Method under test: {@link SolarGridService#findById(long)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/types.csv")
    void findById_should_returnTypeResponse_when_TypeIsFound(long id, String name, String description) {
        SolarGrid solarGrid = new SolarGrid();
        solarGrid.setId(id);
        solarGrid.setName(name);
        solarGrid.setDescription(description);

        SolarGridResponse solarGridResponse = new SolarGridResponse();
        solarGridResponse.setId(id);
        solarGridResponse.setName(name);
        solarGridResponse.setDescription(description);

        when(solarGridRepository.findById(id)).thenReturn(Optional.of(solarGrid));
        when(solarGridResponseMapper.toDto(solarGrid)).thenReturn(solarGridResponse);

        SolarGridResponse result = solarGridService.findById(id);

        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
        assertEquals(description, result.getDescription());
    }

    /**
     * Method under test: {@link SolarGridService#findById(long)}
     */
    @Test
    void findById_should_throwNoSuchElementFoundException_when_TypeIsNotFound() {
        long id = 101L;
        when(solarGridRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> {
            solarGridService.findById(id);
        });

        verify(solarGridRepository).findById(id);
    }

    /**
     * Method under test: {@link SolarGridService#getById(long)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/types.csv")
    void getById_should_returnType_when_TypeIsFound(long id, String name, String description) {
        SolarGrid solarGrid = new SolarGrid();
        solarGrid.setId(id);
        solarGrid.setName(name);
        solarGrid.setDescription(description);

        when(solarGridRepository.findById(id)).thenReturn(Optional.of(solarGrid));

        SolarGrid result = solarGridService.getById(id);

        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
        assertEquals(description, result.getDescription());
    }

    /**
     * Method under test: {@link SolarGridService#getById(long)}
     */
    @Test
    void getById_should_throwNoSuchElementFoundException_when_TypeIsNotFound() {
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
    @ParameterizedTest
    @CsvFileSource(resources = "/data/types.csv")
    void findAll_should_returnTypeResponsePage_when_TypeIsFound(long id, String name, String description) {
        SolarGrid solarGrid = new SolarGrid();
        solarGrid.setId(id);
        solarGrid.setName(name);
        solarGrid.setDescription(description);

        ArrayList<SolarGrid> solarGridList = new ArrayList<>();
        solarGridList.add(solarGrid);
        PageImpl<SolarGrid> pageImpl = new PageImpl<>(solarGridList);
        Pageable pageable = PageRequest.of(0, 10);

        SolarGridResponse solarGridResponse = new SolarGridResponse();
        solarGridResponse.setId(id);
        solarGridResponse.setName(name);
        solarGridResponse.setDescription(description);

        when(solarGridRepository.findAll(pageable)).thenReturn(pageImpl);
        when(solarGridResponseMapper.toDto(solarGrid)).thenReturn(solarGridResponse);

        Page<SolarGridResponse> result = solarGridService.findAll(pageable);

        verify(solarGridResponseMapper).toDto(any());
        assertEquals(1, result.getContent().size());
        assertEquals(id, result.getContent().get(0).getId());
        assertEquals(name, result.getContent().get(0).getName());
        assertEquals(description, result.getContent().get(0).getDescription());
    }

    /**
     * Method under test: {@link SolarGridService#findAll(Pageable)}
     */
    @Test
    void findAll_should_throwNoSuchElementFoundException_when_TypeIsNotFound() {
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
    @ParameterizedTest
    @CsvFileSource(resources = "/data/types.csv")
    void create_should_returnCommandResponse_when_TypeIsCreated(long id, String name, String description) {
        SolarGrid solarGrid = new SolarGrid();
        solarGrid.setId(id);
        solarGrid.setName(name);
        solarGrid.setDescription(description);

        SolarGridRequest request = new SolarGridRequest();
        request.setId(id);
        request.setName(name);
        request.setDescription(description);

        when(solarGridRepository.existsByNameIgnoreCase(name)).thenReturn(false);
        when(solarGridRequestMapper.toEntity(request)).thenReturn(solarGrid);

        CommandResponse result = solarGridService.create(request);
        verify(solarGridRepository).save(typeCaptor.capture());
        SolarGrid capturedSolarGrid = typeCaptor.getValue();

        assertEquals(capturedSolarGrid.getId(), result.id());
        assertEquals(name, capturedSolarGrid.getName());
        assertEquals(description, capturedSolarGrid.getDescription());
    }

    /**
     * Method under test: {@link SolarGridService#create(SolarGridRequest)}
     */
    @Test
    void create_should_throwElementAlreadyExistsException_when_TypeAlreadyExists() {
        SolarGridRequest request = new SolarGridRequest();
        request.setName("Dog");

        when(solarGridRepository.existsByNameIgnoreCase("Dog")).thenReturn(true);

        assertThrows(ElementAlreadyExistsException.class, () -> {
            solarGridService.create(request);
        });

        verify(solarGridRepository, never()).save(any());
    }

    /**
     * Method under test: {@link SolarGridService#update(SolarGridRequest)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/types.csv")
    void update_should_returnCommandResponse_when_TypeNameIsSame(long id, String name, String description, String updatedName, String updatedDescription) {
        SolarGrid solarGrid = new SolarGrid();
        solarGrid.setId(id);
        solarGrid.setName(name);
        solarGrid.setDescription(description);

        SolarGridRequest request = new SolarGridRequest();
        request.setId(id);
        request.setName(name);
        request.setDescription(updatedDescription);

        SolarGrid updatedSolarGrid = new SolarGrid();
        updatedSolarGrid.setId(id);
        updatedSolarGrid.setName(updatedName);
        updatedSolarGrid.setDescription(updatedDescription);

        when(solarGridRepository.findById(id)).thenReturn(Optional.of(solarGrid));
        when(solarGridRequestMapper.toEntity(request)).thenReturn(updatedSolarGrid);

        CommandResponse result = solarGridService.update(request);
        verify(solarGridRepository).save(typeCaptor.capture());
        SolarGrid capturedSolarGrid = typeCaptor.getValue();

        assertEquals(capturedSolarGrid.getId(), result.id());
        assertEquals(updatedName, capturedSolarGrid.getName());
        assertEquals(updatedDescription, capturedSolarGrid.getDescription());
    }

    /**
     * Method under test: {@link SolarGridService#update(SolarGridRequest)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/types.csv")
    void update_should_returnCommandResponse_when_TypeNameIsDifferent(long id, String name, String description, String updatedName, String updatedDescription) {
        SolarGrid solarGrid = new SolarGrid();
        solarGrid.setId(id);
        solarGrid.setName(name);
        solarGrid.setDescription(description);

        SolarGridRequest request = new SolarGridRequest();
        request.setId(id);
        request.setName(updatedName);
        request.setDescription(updatedDescription);

        SolarGrid updatedSolarGrid = new SolarGrid();
        updatedSolarGrid.setId(id);
        updatedSolarGrid.setName(updatedName);
        updatedSolarGrid.setDescription(updatedDescription);

        when(solarGridRepository.findById(id)).thenReturn(Optional.of(solarGrid));
        when(solarGridRepository.existsByNameIgnoreCase(updatedName)).thenReturn(false);
        when(solarGridRequestMapper.toEntity(request)).thenReturn(updatedSolarGrid);

        CommandResponse result = solarGridService.update(request);
        verify(solarGridRepository).save(typeCaptor.capture());
        SolarGrid capturedSolarGrid = typeCaptor.getValue();

        assertEquals(capturedSolarGrid.getId(), result.id());
        assertEquals(updatedName, capturedSolarGrid.getName());
        assertEquals(updatedDescription, capturedSolarGrid.getDescription());
    }

    /**
     * Method under test: {@link SolarGridService#update(SolarGridRequest)}
     */
    @Test
    void update_should_throwNoSuchElementFoundException_when_TypeIsNotFound() {
        SolarGridRequest request = new SolarGridRequest();
        request.setId(101L);
        request.setName("Dog");

        when(solarGridRepository.findById(101L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> {
            solarGridService.update(request);
        });

        verify(solarGridRepository, never()).existsByNameIgnoreCase(any());
        verify(solarGridRepository, never()).save(any());
    }

    /**
     * Method under test: {@link SolarGridService#update(SolarGridRequest)}
     */
    @Test
    void update_should_throwElementAlreadyExistsException_when_TypeAlreadyExists() {
        SolarGrid solarGrid = new SolarGrid();
        solarGrid.setId(101L);
        solarGrid.setName("Cat");

        SolarGridRequest request = new SolarGridRequest();
        request.setId(101L);
        request.setName("Dog");

        when(solarGridRepository.findById(101L)).thenReturn(Optional.of(solarGrid));
        when(solarGridRepository.existsByNameIgnoreCase("Dog")).thenReturn(true);

        assertThrows(ElementAlreadyExistsException.class, () -> {
            solarGridService.update(request);
        });

        verify(solarGridRepository, never()).save(any());
    }

    /**
     * Method under test: {@link SolarGridService#deleteById(long)}
     */
    @Test
    void deleteById_should_deleteType_when_TypeIsFound() {
        SolarGrid solarGrid = new SolarGrid();
        solarGrid.setId(101L);
        solarGrid.setName("Cat");

        when(solarGridRepository.findById(101L)).thenReturn(Optional.of(solarGrid));

        solarGridService.deleteById(101L);
        verify(solarGridRepository).delete(typeCaptor.capture());
        SolarGrid capturedSolarGrid = typeCaptor.getValue();

        assertEquals(101L, capturedSolarGrid.getId());
        assertEquals("Cat", capturedSolarGrid.getName());
    }

    /**
     * Method under test: {@link SolarGridService#deleteById(long)}
     */
    @Test
    void deleteById_should_throwNoSuchElementFoundException_when_TypeIsNotFound() {
        long id = 101L;
        when(solarGridRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> {
            solarGridService.deleteById(id);
        });

        verify(solarGridRepository).findById(id);
        verify(solarGridRepository, never()).delete(any());
    }
}
