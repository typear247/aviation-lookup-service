package co.za.aviationservice.service;

import co.za.aviationservice.client.ProviderAClient;
import co.za.aviationservice.client.ProviderBClient;
import co.za.aviationservice.exception.AirportNotFoundException;
import co.za.aviationservice.utils.AviationProviderFactory;
import co.za.aviationservice.model.AirportInformation;
import co.za.aviationservice.model.AirportResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(
        properties = {
                "aviation.provider=providerA",
                "resilience4j.circuitbreaker.instances.aviationApi.slidingWindowSize=3",
                "resilience4j.retry.instances.aviationApi.max-attempts=1"
        }
)
class AirportServiceTest {

    @Autowired
    private AirportService airportService;

    @MockBean
    private ProviderAClient providerA;

    @MockBean
    private ProviderBClient providerB;

    @MockBean
    private AviationProviderFactory aviationProviderFactory;

    // ---------------
    // SUCCESS TEST
    // ---------------
    @Test
    void shouldReturnAirportFromProviderA() {

        // mock provider names (REQUIRED!)
        when(providerA.providerName()).thenReturn("providernameA");
        when(providerB.providerName()).thenReturn("providernameB");

        // factory returns providerA
        when(aviationProviderFactory.resolve()).thenReturn(providerA);

        AirportResponse response = new AirportResponse();
        response.put("KATL", List.of(
                AirportInformation.builder()
                        .icaoIdent("KATL")
                        .facilityName("ATL AIRPORT")
                        .build()
        ));

        when(providerA.getAirportByIcao("KATL")).thenReturn(response);

        AirportResponse result = airportService.getAirportByIcao("KATL");

        assertEquals("ATL AIRPORT",
                result.get("KATL").get(0).getFacilityName());
    }

    // ---------------
    // FALLBACK → Provider B
    // ---------------
    @Test
    void shouldFallbackToProviderBWhenProviderAFails() {

        // provider names REQUIRED
        when(providerA.providerName()).thenReturn("providernameA");
        when(providerB.providerName()).thenReturn("providernameB");

        // first resolve() → providerA
        when(aviationProviderFactory.resolve()).thenReturn(providerA);

        when(providerA.getAirportByIcao("KATL"))
                .thenThrow(new AirportNotFoundException("Provider A failed"));

        // fallback should call providerB
        AirportResponse responseB = new AirportResponse();
        responseB.put("KATL", List.of(
                AirportInformation.builder()
                        .icaoIdent("KATL")
                        .facilityName("Provider B Airport")
                        .build()
        ));

        when(providerB.getAirportByIcao("KATL")).thenReturn(responseB);

        AirportResponse result = airportService.getAirportByIcao("KATL");

        assertEquals("Provider B Airport",
                result.get("KATL").get(0).getFacilityName());
    }

    // ---------------
    // STATIC FALLBACK
    // ---------------
    @Test
    void shouldReturnStaticFallbackWhenBothProvidersFail() {

        when(providerA.providerName()).thenReturn("providernameA");
        when(providerB.providerName()).thenReturn("providernameB");

        when(aviationProviderFactory.resolve()).thenReturn(providerA);

        when(providerA.getAirportByIcao("KJFK"))
                .thenThrow(new AirportNotFoundException("A failed"));

        when(providerB.getAirportByIcao("KJFK"))
                .thenThrow(new RuntimeException("B failed"));

        AirportResponse result = airportService.getAirportByIcao("KJFK");

        assertEquals("Service temporarily unavailable",
                result.get("KJFK").get(0).getFacilityName());
    }
}