package com.fastned.solarcharging.controller;

import com.fastned.solarcharging.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SolarSimulatorControllerTest extends IntegrationTest {


    /**
     * Method under test: {@link SolarSimulatorController#generateOutputDuringDays(Integer, Authentication)}
     */
    @Test
    @WithMockUser(username = "jake", roles = {"USER"})
    void findAll_SolarSimulator_TotalOutput_should_returnStatusIsOk() throws Exception {
        mvc.perform((get("/api/v1/solar-simulator/output/{days}", 320)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalOutputInKWh").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalOutputInKWh").isNumber());
    }

    /**
     * Method under test: {@link SolarSimulatorController#generateOutputDuringDays(Integer, Authentication)}
     */
    @Test
    @WithMockUser(username = "jake", roles = {"USER"})
    void findAll_SolarSimulator_TotalOutput_isNumber_should_returnStatusIsOk() throws Exception {
        mvc.perform((get("/api/v1/solar-simulator/output/{days}", 320)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalOutputInKWh").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalOutputInKWh").isNumber());
    }

    /**
     * Method under test: {@link SolarSimulatorController#generateOutputDuringDays(Integer, Authentication)}
     */
    @Test
    @WithMockUser(username = "jake", roles = {"USER"})
    void findAll_SolarSimulator_TotalOutput_Response0_should_returnStatusIsOk() throws Exception {
        mvc.perform((get("/api/v1/solar-simulator/output/{days}", 58)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalOutputInKWh").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalOutputInKWh").value(0.0));
    }


    /**
     * Method under test: {@link SolarSimulatorController#generateOutputDuringDays(Integer, Authentication)}
     */
    @Test
    @WithMockUser(username = "jake", roles = {"USER"})
    void findAll_SolarSimulator_TotalNetworks_Networks_should_returnStatusIsOk() throws Exception {
        mvc.perform((get("/api/v1/solar-simulator/output/{days}", 320)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalOutputInKWh").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalOutputInKWh").value(40.0));
    }


}
