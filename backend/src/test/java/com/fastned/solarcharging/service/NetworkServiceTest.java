package com.fastned.solarcharging.service;

import com.fastned.solarcharging.dto.mapper.NetworkRequestMapper;
import com.fastned.solarcharging.dto.mapper.NetworkResponseMapper;
import com.fastned.solarcharging.dto.request.NetworkRequest;
import com.fastned.solarcharging.dto.request.TypeSetRequest;
import com.fastned.solarcharging.dto.response.CommandResponse;
import com.fastned.solarcharging.dto.response.NetworkResponse;
import com.fastned.solarcharging.dto.response.SolarGridResponse;
import com.fastned.solarcharging.dto.response.UserResponse;
import com.fastned.solarcharging.exception.NoSuchElementFoundException;
import com.fastned.solarcharging.model.Network;
import com.fastned.solarcharging.model.SolarGrid;
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
    private ArgumentCaptor<Network> petCaptor;

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
    @ParameterizedTest
    @CsvFileSource(resources = "/data/pets.csv")
    void findAllByUserId_should_returnPetResponseList_when_PetIsFound(long id, String name, long typeId, String typeName, long userId,
                                                              String firstName, String lastName, String fullName, String username) {
        Network network = new Network();
        network.setId(id);
        network.setName(name);

        ArrayList<Network> networkList = new ArrayList<>();
        networkList.add(network);

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
        networkResponse.setType(solarGridResponse);
        networkResponse.setUser(userResponse);

        when(networkRepository.findAllByUserId(userId)).thenReturn(networkList);
        when(networkResponseMapper.toDto(network)).thenReturn(networkResponse);

        List<NetworkResponse> result = networkService.findAllByUserId(userId);

        verify(networkResponseMapper).toDto(network);
        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
        assertEquals(name, result.get(0).getName());
        assertEquals(typeId, result.get(0).getType().getId());
        assertEquals(typeName, result.get(0).getType().getName());
        assertEquals(userId, result.get(0).getUser().getId());
        assertEquals(firstName, result.get(0).getUser().getFirstName());
        assertEquals(lastName, result.get(0).getUser().getLastName());
        assertEquals(fullName, result.get(0).getUser().getFullName());
        assertEquals(username, result.get(0).getUser().getUsername());
    }

    /**
     * Method under test: {@link NetworkService#findAllByUserId(long)}
     */
    @Test
    void findAllByUserId_should_throwNoSuchElementFoundException_when_PetIsNotFound() {
        long id = 101L;
        when(networkRepository.findAllByUserId(id)).thenReturn(Collections.emptyList());

        assertThrows(NoSuchElementFoundException.class, () -> {
            networkService.findAllByUserId(id);
        });

        verify(networkRepository).findAllByUserId(id);
    }

    /**
     * Method under test: {@link NetworkService#findAllByType(TypeSetRequest)}
     */
    @Test
    void findAllByType_should_returnFilteredTypes_when_TypesNotEmpty() {
        SolarGrid solarGrid1 = new SolarGrid();
        solarGrid1.setId(101L);
        solarGrid1.setName("Dog");
        SolarGrid solarGrid2 = new SolarGrid();
        solarGrid2.setId(102L);
        solarGrid2.setName("Cat");
        SolarGrid solarGrid3 = new SolarGrid();
        solarGrid3.setId(103L);
        solarGrid3.setName("Bird");

        Network network1 = new Network();
        network1.setType(solarGrid1);
        Network network2 = new Network();
        network2.setType(solarGrid2);
        Network network3 = new Network();
        network3.setType(solarGrid3);

        Set<Long> types = new HashSet<>(Arrays.asList(101L, 102L));
        TypeSetRequest typeSetRequest = new TypeSetRequest();
        typeSetRequest.setIds(types);

        ArrayList<Network> networkList = new ArrayList<>();
        networkList.add(network1);
        networkList.add(network2);
        networkList.add(network3);

        when(networkRepository.findAll()).thenReturn(networkList);

        Map<String, Long> result = networkService.findAllByType(typeSetRequest);

        assertEquals(2, result.size());
        assertEquals(1, result.get("Dog"));
        assertEquals(1, result.get("Cat"));
    }

    /**
     * Method under test: {@link NetworkService#findAllByType(TypeSetRequest)}
     */
    @Test
    void findAllByType_should_returnAllTypes_when_TypesEmpty() {
        SolarGrid solarGrid1 = new SolarGrid();
        solarGrid1.setId(101L);
        solarGrid1.setName("Dog");
        SolarGrid solarGrid2 = new SolarGrid();
        solarGrid2.setId(102L);
        solarGrid2.setName("Cat");
        SolarGrid solarGrid3 = new SolarGrid();
        solarGrid3.setId(103L);
        solarGrid3.setName("Bird");

        Network network1 = new Network();
        network1.setType(solarGrid1);
        Network network2 = new Network();
        network2.setType(solarGrid2);
        Network network3 = new Network();
        network3.setType(solarGrid3);

        Set<Long> types = new HashSet<>();
        TypeSetRequest typeSetRequest = new TypeSetRequest();
        typeSetRequest.setIds(types);

        ArrayList<Network> networkList = new ArrayList<>();
        networkList.add(network1);
        networkList.add(network2);
        networkList.add(network3);

        when(networkRepository.findAll()).thenReturn(networkList);

        Map<String, Long> result = networkService.findAllByType(typeSetRequest);

        assertEquals(3, result.size());
        assertEquals(1, result.get("Dog"));
        assertEquals(1, result.get("Cat"));
        assertEquals(1, result.get("Bird"));
    }

    /**
     * Method under test: {@link NetworkService#create(NetworkRequest)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/pets.csv")
    void create_should_returnCommandResponse_when_PetIsCreated(long id, String name, long typeId, String typeName, long userId,
                                                               String firstName, String lastName, String fullName, String username) {
        SolarGrid solarGrid = new SolarGrid();
        solarGrid.setId(typeId);
        solarGrid.setName(typeName);

        User user = new User();
        user.setId(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);

        NetworkRequest request = new NetworkRequest();
        request.setId(id);
        request.setName(name);
        request.setTypeId(solarGrid.getId());
        request.setIdUser(user.getId());

        Network network = new Network();
        network.setId(id);
        network.setName(name);
        network.setType(solarGrid);
        network.setUser(user);

        when(networkRequestMapper.toEntity(request)).thenReturn(network);
        when(solarGridService.getById(solarGrid.getId())).thenReturn(solarGrid);
        when(userService.getById(user.getId())).thenReturn(user);

        CommandResponse result = networkService.create(request);
        verify(networkRepository).save(petCaptor.capture());
        Network capturedNetwork = petCaptor.getValue();

        assertEquals(capturedNetwork.getId(), result.id());
        assertEquals(name, capturedNetwork.getName());
        assertEquals(typeId, capturedNetwork.getType().getId());
        assertEquals(typeName, capturedNetwork.getType().getName());
        assertEquals(userId, capturedNetwork.getUser().getId());
        assertEquals(firstName, capturedNetwork.getUser().getFirstName());
        assertEquals(lastName, capturedNetwork.getUser().getLastName());
        assertEquals(username, capturedNetwork.getUser().getUsername());
    }

    /**
     * Method under test: {@link NetworkService#update(NetworkRequest)}
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data/pets.csv")
    void update_should_returnCommandResponse_when_PetIsFound(long id, String name, long typeId, String typeName, long userId,
                                                             String firstName, String lastName, String fullName, String username) {
        SolarGrid solarGrid = new SolarGrid();
        solarGrid.setId(typeId);
        solarGrid.setName(typeName);

        User user = new User();
        user.setId(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);

        NetworkRequest request = new NetworkRequest();
        request.setId(id);
        request.setName(name);
        request.setTypeId(solarGrid.getId());
        request.setIdUser(user.getId());

        Network network = new Network();
        network.setId(id);
        network.setName(name);
        network.setType(solarGrid);
        network.setUser(user);

        when(networkRepository.existsById(request.getId())).thenReturn(true);
        when(networkRequestMapper.toEntity(request)).thenReturn(network);
        when(solarGridService.getById(solarGrid.getId())).thenReturn(solarGrid);
        when(userService.getById(user.getId())).thenReturn(user);

        CommandResponse result = networkService.update(request);
        verify(networkRepository).save(petCaptor.capture());
        Network capturedNetwork = petCaptor.getValue();

        assertEquals(capturedNetwork.getId(), result.id());
        assertEquals(name, capturedNetwork.getName());
        assertEquals(typeId, capturedNetwork.getType().getId());
        assertEquals(typeName, capturedNetwork.getType().getName());
        assertEquals(userId, capturedNetwork.getUser().getId());
        assertEquals(firstName, capturedNetwork.getUser().getFirstName());
        assertEquals(lastName, capturedNetwork.getUser().getLastName());
        assertEquals(username, capturedNetwork.getUser().getUsername());
    }

    /**
     * Method under test: {@link NetworkService#update(NetworkRequest)}
     */
    @Test
    void update_should_throwNoSuchElementFoundException_when_PetIsNotExist() {
        NetworkRequest request = new NetworkRequest();
        request.setId(101L);
        request.setTypeId(201L);
        request.setIdUser(301L);
        when(networkRepository.existsById(101L)).thenReturn(false);

        assertThrows(NoSuchElementFoundException.class, () -> {
            networkService.update(request);
        });

        verify(solarGridService, never()).getById(201L);
        verify(userService, never()).getById(301L);
        verify(networkRepository, never()).getById(101L);
    }

    /**
     * Method under test: {@link NetworkService#deleteById(long)}
     */
    @Test
    void deleteById_should_deletePet_when_PetIsFound() {
        Network network = new Network();
        network.setId(101L);
        network.setName("Daisy");
        network.setType(new SolarGrid());
        network.setUser(new User());

        when(networkRepository.findById(101L)).thenReturn(Optional.of(network));

        networkService.deleteById(101L);
        verify(networkRepository).delete(petCaptor.capture());
        Network capturedNetwork = petCaptor.getValue();

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
