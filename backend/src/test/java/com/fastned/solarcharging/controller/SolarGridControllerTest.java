package com.fastned.solarcharging.controller;

import com.fastned.solarcharging.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SolarGridControllerTest extends IntegrationTest {

    /**
     * Method under test: {@link SolarGridController#findById(long)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findById_should_returnStatusIsOk_when_IsFound() throws Exception {
        mvc.perform((get("/api/v1/solar-grid/{id}", 1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", equalTo("Sao Paulo")));
    }

    /**
     * Method under test: {@link SolarGridController#findById(long)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findById_should_returnStatusIsNotFound_when_IsNotFound() throws Exception {
        mvc.perform((get("/api/v1/solar-grid/{id}", 999)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    /**
     * Method under test: {@link SolarGridController#findAll(Pageable)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findAll_SolarGrid_CheckContents_should_returnStatusIsOk() throws Exception {
        mvc.perform((get("/api/v1/solar-grid")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[*].name").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.content[1].name").value("Brasilia"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements").value(2));
    }

    /**
     * Method under test: {@link SolarGridController#findByUserId(long)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findAllByUserId_should_returnStatusIsOk_when_IsFound() throws Exception {
        mvc.perform((get("/api/v1/solar-grid/user/{userId}", 2)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("Sao Paulo"));
    }

    /**
     * Method under test: {@link SolarGridController#findByUserId(long)}
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void findAllByUserId_should_returnStatusIsNotFound_when_IsNotFound() throws Exception {
        mvc.perform((get("/api/v1/solar-grid/user/{id}", 999)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }



}
