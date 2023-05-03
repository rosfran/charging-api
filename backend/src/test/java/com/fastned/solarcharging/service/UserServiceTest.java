package com.fastned.solarcharging.service;

import com.fastned.solarcharging.dto.mapper.UserRequestMapper;
import com.fastned.solarcharging.dto.mapper.UserResponseMapper;
import com.fastned.solarcharging.dto.request.ProfileRequest;
import com.fastned.solarcharging.dto.request.UserRequest;
import com.fastned.solarcharging.dto.response.CommandResponse;
import com.fastned.solarcharging.dto.response.RoleResponse;
import com.fastned.solarcharging.dto.response.UserResponse;
import com.fastned.solarcharging.exception.ElementAlreadyExistsException;
import com.fastned.solarcharging.exception.NoSuchElementFoundException;
import com.fastned.solarcharging.model.Role;
import com.fastned.solarcharging.model.RoleType;
import com.fastned.solarcharging.model.User;
import com.fastned.solarcharging.repository.UserRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit Test for UserService methods
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRequestMapper userRequestMapper;

    @Mock
    private UserResponseMapper userResponseMapper;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    /**
     * Method under test: {@link UserService#findById(long)}
     */
    @Test
    void findById_should_throwNoSuchElementFoundException_when_UserIsNotFound() {
        long id = 101L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> {
            userService.findById(id);
        });

        verify(userRepository).findById(id);
    }


    /**
     * Method under test: {@link UserService#getById(long)}
     */
    @Test
    void getById_should_throwNoSuchElementFoundException_when_UserIsNotFound() {
        long id = 101L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> {
            userService.getById(id);
        });

        verify(userRepository).findById(id);
    }

    /**
     * Method under test: {@link UserService#findAll(Pageable)}
     */
    @Test
    void findAll_should_throwNoSuchElementFoundException_when_UserIsNotFound() {
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(new ArrayList<>()));

        assertThrows(NoSuchElementFoundException.class, () -> {
            userService.findAll(pageable);
        });

        verify(userRepository).findAll(pageable);
    }

    /**
     * Method under test: {@link UserService#create(UserRequest)}
     */
    @Test
    void create_should_throwElementAlreadyExistsException_when_UserAlreadyExists() {
        UserRequest request = new UserRequest();
        request.setUsername("username");

        when(userRepository.existsByUsernameIgnoreCase("username")).thenReturn(true);

        assertThrows(ElementAlreadyExistsException.class, () -> {
            userService.create(request);
        });

        verify(userRepository, never()).save(any());
    }

    /**
     * Method under test: {@link UserService#update(ProfileRequest)}
     */
    @Test
    void update_should_throwNoSuchElementFoundException_when_UserIsNotFound() {
        ProfileRequest request = new ProfileRequest();
        request.setId(101L);

        when(userRepository.findById(101L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> {
            userService.update(request);
        });

        verify(userRepository, never()).save(any());
    }

    /**
     * Method under test: {@link UserService#updateFullName(ProfileRequest)}
     */
    @Test
    void updateFullName_should_throwNoSuchElementFoundException_when_UserIsNotFound() {
        ProfileRequest request = new ProfileRequest();
        request.setId(101L);

        when(userRepository.findById(101L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> {
            userService.updateFullName(request);
        });

        verify(userRepository, never()).save(any());
    }

    /**
     * Method under test: {@link UserService#deleteById(long)}
     */
    @Test
    void deleteById_should_throwNoSuchElementFoundException_when_UserIsNotFound() {
        long id = 101L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementFoundException.class, () -> {
            userService.deleteById(id);
        });

        verify(userRepository).findById(id);
        verify(userRepository, never()).delete(any());
    }
}
