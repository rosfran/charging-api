package com.fastned.solarcharging.service;

import com.fastned.solarcharging.dto.mapper.NetworkRequestMapper;
import com.fastned.solarcharging.dto.mapper.NetworkResponseMapper;
import com.fastned.solarcharging.dto.request.NetworkRequest;
import com.fastned.solarcharging.dto.request.TypeSetRequest;
import com.fastned.solarcharging.dto.response.NetworkResponse;
import com.fastned.solarcharging.dto.response.SolarGridResponse;
import com.fastned.solarcharging.dto.response.UserResponse;
import com.fastned.solarcharging.exception.NoSuchElementFoundException;
import com.fastned.solarcharging.model.Network;
import com.fastned.solarcharging.model.SolarGrid;
import com.fastned.solarcharging.model.State;
import com.fastned.solarcharging.model.User;
import com.fastned.solarcharging.repository.NetworkRepository;
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

    @Captor
    private ArgumentCaptor<Network> networkCaptor;

    /**
     * Method under test: {@link NetworkService#findById(long)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/pets.csv")
    void findById_should_returnPetResponse_when_PetIsFound(long id, String name, long typeId, String typeName, long userId,
                                                           String firstName, String lastName, String fullName, String username) {
        Network network = new Network();
        network.setId(id);
        network.setName(name);

        SolarGridResponse solarGridResponse = new SolarGridResponse();
        solarGridResponse.setId(typeId);
        solarGridResponse.setName(typeName);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setFirstName(firstName);
        userResponse.setLastName(lastName);
        userResponse.setUsername(username);
        userResponse.setFullName(fullName);

        NetworkResponse networkResponse = new NetworkResponse();
        networkResponse.setId(id);
        networkResponse.setName(name);
        networkResponse.setUser(userResponse);
        networkResponse.setType(solarGridResponse);

        when(networkRepository.findById(id)).thenReturn(Optional.of(network));
        when(networkResponseMapper.toDto(network)).thenReturn(networkResponse);

        NetworkResponse result = networkService.findById(id);

        assertEquals(id, result.getId());
        assertEquals(name, result.getName());
        assertEquals(typeId, result.getType().getId());
        assertEquals(typeName, result.getType().getName());
        assertEquals(userId, result.getUser().getId());
        assertEquals(firstName, result.getUser().getFirstName());
        assertEquals(lastName, result.getUser().getLastName());
        assertEquals(fullName, result.getUser().getFullName());
        assertEquals(username, result.getUser().getUsername());
    }

    /**
     * Method under test: {@link NetworkService#findById(long)}
     */
    @Test
    void findById_should_throwNoSuchElementFoundException_when_PetIsNotFound() {
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
    @ParameterizedTest
    @CsvFileSource(resources = "/data/pets.csv")
    void findAll_should_returnPetResponsePage_when_PetIsFound(long id, String name, long typeId, String typeName, long userId,
                                                              String firstName, String lastName, String fullName, String username) {
        Network network = new Network();
        network.setId(id);
        network.setName(name);

        ArrayList<Network> networkList = new ArrayList<>();
        networkList.add(network);
        PageImpl<Network> pageImpl = new PageImpl<>(networkList);
        Pageable pageable = PageRequest.of(0, 10);

        SolarGridResponse solarGridResponse = new SolarGridResponse();
        solarGridResponse.setId(typeId);
        solarGridResponse.setName(typeName);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setFirstName(firstName);
        userResponse.setLastName(lastName);
        userResponse.setUsername(username);
        userResponse.setFullName(fullName);

        NetworkResponse networkResponse = new NetworkResponse();
        networkResponse.setId(id);
        networkResponse.setName(name);
        networkResponse.setUser(userResponse);
        networkResponse.setType(solarGridResponse);

        when(networkRepository.findAll(pageable)).thenReturn(pageImpl);
        when(networkResponseMapper.toDto(network)).thenReturn(networkResponse);

        Page<NetworkResponse> result = networkService.findAll(pageable);

        verify(networkResponseMapper).toDto(any());
        assertEquals(1, result.getContent().size());
        assertEquals(id, result.getContent().get(0).getId());
        assertEquals(name, result.getContent().get(0).getName());
        assertEquals(typeId, result.getContent().get(0).getType().getId());
        assertEquals(typeName, result.getContent().get(0).getType().getName());
        assertEquals(userId, result.getContent().get(0).getUser().getId());
        assertEquals(firstName, result.getContent().get(0).getUser().getFirstName());
        assertEquals(lastName, result.getContent().get(0).getUser().getLastName());
        assertEquals(fullName, result.getContent().get(0).getUser().getFullName());
        assertEquals(username, result.getContent().get(0).getUser().getUsername());
    }

    /**
     * Method under test: {@link NetworkService#findAll(Pageable)}
     */
    @Test
    void findAll_should_throwNoSuchElementFoundException_when_PetIsNotFound() {
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
        when(networkRepository.findAllByIdUser(id)).thenReturn(Collections.emptyList());

        assertThrows(NoSuchElementFoundException.class, () -> {
            networkService.findAllByUserId(id);
        });

        verify(networkRepository).findAllByIdUser(id);
    }

    /**
     * Method under test: {@link NetworkService#findAllByUserId(long)} (TypeSetRequest)}
     */
    @Test
    void findAllByType_should_returnFilteredTypes_when_TypesNotEmpty() {

        Network network1 = new Network();
        final User user = new User();
        user.setId(12l);
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
        TypeSetRequest typeSetRequest = new TypeSetRequest();
        typeSetRequest.setIds(types);

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
    void deleteById_should_deletePet_when_PetIsFound() {
        Network network = new Network();
        network.setId(101L);
        network.setName("Daisy");
        network.setUser(new User());

        when(networkRepository.findById(101L)).thenReturn(Optional.of(network));

        networkService.deleteById(101L);
        verify(networkRepository).delete(networkCaptor.capture());
        Network capturedNetwork = networkCaptor.getValue();

        assertEquals(101L, capturedNetwork.getId());
        assertEquals("Daisy", capturedNetwork.getName());
    }

    /**
     * Method under test: {@link NetworkService#deleteById(long)}
     */
    @Test
    void deleteById_should_throwNoSuchElementFoundException_when_PetIsNotFound() {
        long id = 101L;
        when(networkRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> {
            networkService.deleteById(id);
        });

        verify(networkRepository).findById(id);
        verify(networkRepository, never()).delete(any());
    }
}
