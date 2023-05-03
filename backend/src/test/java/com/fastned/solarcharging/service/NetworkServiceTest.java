package com.fastned.solarcharging.service;

import com.fastned.solarcharging.dto.mapper.NetworkRequestMapper;
import com.fastned.solarcharging.dto.mapper.NetworkResponseMapper;
import com.fastned.solarcharging.dto.mapper.UserRequestMapper;
import com.fastned.solarcharging.dto.request.NetworkRequest;
import com.fastned.solarcharging.dto.request.UserRequest;
import com.fastned.solarcharging.dto.response.NetworkResponse;
import com.fastned.solarcharging.exception.NoSuchElementFoundException;
import com.fastned.solarcharging.model.Network;
import com.fastned.solarcharging.model.SolarGrid;
import com.fastned.solarcharging.model.State;
import com.fastned.solarcharging.model.User;
import com.fastned.solarcharging.repository.NetworkRepository;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit Test for NetworkService methods
 */
@ExtendWith(MockitoExtension.class)
class NetworkServiceTest {

    @InjectMocks
    private NetworkService networkService;

    @Mock
    private NetworkRepository networkRepository;

    @Mock
    private NetworkRequestMapper networkRequestMapper;

    @Mock
    private NetworkResponseMapper networkResponseMapper;

    @Mock
    private SolarGridService solarGridService;

    @Mock
    private UserService userService;

    @Mock
    private UserRequestMapper userRequestMapper;

    @Captor
    private ArgumentCaptor<Network> networkCaptor;

    /**
     * Method under test: {@link NetworkService#findById(long)}
     */
    @Test
    void findById_should_throwNoSuchElementFoundException_when_IsNotFound() {
        long id = 101L;
        when(networkRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> {
            networkService.findById(id);
        });

        verify(networkRepository).findById(id);
    }

    /**
     * Method under test: {@link NetworkService#findAll(Pageable)}
     */
    @Test
    void findAll_should_throwNoSuchElementFoundException_when_IsNotFound() {
        Pageable pageable = PageRequest.of(0, 10);
        when(networkRepository.findAll(pageable)).thenReturn(new PageImpl<>(new ArrayList<>()));

        assertThrows(NoSuchElementFoundException.class, () -> {
            networkService.findAll(pageable);
        });

        verify(networkRepository).findAll(pageable);
    }

    /**
     * Method under test: {@link NetworkService#findAllByUserId(long)}
     */
    @Test
    void findAllByUserId_should_throwNoSuchElementFoundException_when_NetworkIsNotFound() {
        long id = 101L;
        when(networkRepository.findAllByUserId(id)).thenReturn(Collections.emptyList());

        assertThrows(NoSuchElementFoundException.class, () -> {
            networkService.findAllByUserId(id);
        });

        verify(networkRepository).findAllByUserId(id);
    }

    /**
     * Method under test: {@link NetworkService#findAllByUserId(long)} (TypeSetRequest)}
     */
    @Test
    void findAllByType_should_returnFilteredTypes_when_TypesNotEmpty() {

        Network network1 = new Network();
        final User user = new User();
        user.setId(12l);

        userService.create( userRequestMapper.toDto(user) );
        network1.setUser(user);

        SolarGrid solarGrid1 = new SolarGrid();
        solarGrid1.setNetwork(network1);
        solarGrid1.setId(101L);
        solarGrid1.setName("Amsterdam");

        State state1 = new State();
        state1.setAge(854);

        state1.setSolarGrid(solarGrid1);
        
        SolarGrid solarGrid2 = new SolarGrid();
        solarGrid2.setId(102L);
        solarGrid2.setName("Groningen");


        State state2 = new State();
        state2.setAge(473);

        state2.setSolarGrid(solarGrid2);

        SolarGrid solarGrid3 = new SolarGrid();
        solarGrid3.setId(103L);
        solarGrid3.setName("Maastricht");

        State state3 = new State();
        state3.setAge(854);

        state3.setSolarGrid(solarGrid3);

        Set<Long> types = new HashSet<>(Arrays.asList(101L, 102L));

        ArrayList<Network> networkList = new ArrayList<>();
        networkList.add(network1);

        when(networkRepository.findAll()).thenReturn(networkList);

       List<NetworkResponse> result = networkService.findAllByUserId(user.getId());

        assertEquals(3, result.size());

    }


    /**
     * Method under test: {@link NetworkService#update(NetworkRequest)}
     */
    @Test
    void update_should_throwNoSuchElementFoundException_when_NetworkIsNotExist() {
        NetworkRequest request = new NetworkRequest();
        request.setId(11L);
        request.setIdUser(21L);
        request.setIdUser(31L);
        when(networkRepository.existsById(11L)).thenReturn(false);

        assertThrows(NoSuchElementFoundException.class, () -> {
            networkService.update(request);
        });

        verify(solarGridService, never()).getById(21L);
        verify(userService, never()).getById(31L);
        verify(networkRepository, never()).getById(11L);
    }

    /**
     * Method under test: {@link NetworkService#deleteById(long)}
     */
    @Test
    void deleteById_should_delete_when_NetworkIsFound() {
        Network network = new Network();
        network.setId(101L);
        network.setName("Amsterdam");
        network.setUser(new User());

        when(networkRepository.findById(101L)).thenReturn(Optional.of(network));

        networkService.deleteById(101L);
        verify(networkRepository).delete(networkCaptor.capture());
        Network capturedNetwork = networkCaptor.getValue();

        assertEquals(101L, capturedNetwork.getId());
        assertEquals("Amsterdam", capturedNetwork.getName());
    }

    /**
     * Method under test: {@link NetworkService#deleteById(long)}
     */
    @Test
    void deleteById_should_throwNoSuchElementFoundException_when_NetworkIsNotFound() {
        long id = 101L;
        when(networkRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> {
            networkService.deleteById(id);
        });

        verify(networkRepository).findById(id);
        verify(networkRepository, never()).delete(any());
    }
}
