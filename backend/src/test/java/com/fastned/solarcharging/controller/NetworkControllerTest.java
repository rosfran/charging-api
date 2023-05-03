package com.fastned.solarcharging.controller;

import com.fastned.solarcharging.IntegrationTest;
import com.fastned.solarcharging.dto.request.NetworkRequest;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NetworkControllerTest extends IntegrationTest {

    /**
     * Method under test: {@link NetworkController#findById(long)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findById_should_returnStatusIsOk_when_IsFound() throws Exception {
        mvc.perform((get("/api/v1/network/{id}", 1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", equalTo("Lassie")));
    }

    /**
     * Method under test: {@link NetworkController#findById(long)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findById_should_returnStatusIsNotFound_when_IsNotFound() throws Exception {
        mvc.perform((get("/api/v1/network/{id}", 999)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    /**
     * Method under test: {@link NetworkController#findAll(Pageable)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findAll_should_returnStatusIsOk() throws Exception {
        mvc.perform((get("/api/v1/network")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[*].name").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[3].name").value("Charlie"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements").value(12));
    }

    /**
     * Method under test: {@link NetworkController#findAllByUserId(long)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findAllByUserId_should_returnStatusIsOk_when_IsFound() throws Exception {
        mvc.perform((get("/api/v1/network/users/{userId}", 1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].name").value("Tweety"));
    }

    /**
     * Method under test: {@link NetworkController#findAllByUserId(long)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findAllByUserId_should_returnStatusIsNotFound_when_IsNotFound() throws Exception {
        mvc.perform((get("/api/v1/network/users/{id}", 999)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


}
