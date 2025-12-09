package co.za.aviationservice.controller;

import co.za.aviationservice.exception.AirportNotFoundException;
import co.za.aviationservice.model.AirportInformation;
import co.za.aviationservice.model.AirportResponse;
import co.za.aviationservice.service.AirportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(MockitoExtension.class)
@WebMvcTest(AirportController.class)
class
AirportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AirportService airportService;

    @Test
    void testGetAirportByIcao_success() throws Exception {
        AirportResponse response = new AirportResponse();
        response.put("KATL", List.of(AirportInformation.builder().build()));

        when(airportService.getAirportByIcao("KATL")).thenReturn(response);

        mockMvc.perform(get("/v1/airports/KATL").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.KATL").exists());
    }

    @Test
    void testGetAirportByIcao_validIcao() throws Exception {
        AirportResponse response = new AirportResponse();
        when(airportService.getAirportByIcao("KATL")).thenReturn(response);

        mockMvc.perform(get("/v1/airports/KATL")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAirportByIcao_notFound() throws Exception {
        when(airportService.getAirportByIcao("XXXX"))
                .thenThrow(new AirportNotFoundException("XXXX"));

        mockMvc.perform(get("/v1/airports/XXXX"))
                .andExpect(status().isNotFound());
    }
}
