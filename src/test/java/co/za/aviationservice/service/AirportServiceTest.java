//package co.za.aviationservice.service;
//
//
//import co.za.aviationservice.exception.AirportNotFoundException;
//import co.za.aviationservice.exception.ExternalApiException;
//import co.za.aviationservice.model.AirportResponse;
//import okhttp3.mockwebserver.MockResponse;
//import okhttp3.mockwebserver.MockWebServer;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.util.ReflectionTestUtils;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class AirportServiceTest {
//    private MockWebServer mockWebServer;
//
//    private AviationApiProvider client;
//
//    @BeforeEach
//    void setup() throws IOException {
//        mockWebServer = new MockWebServer();
//        mockWebServer.start();
//
//        WebClient testWebClient = WebClient.builder()
//                .baseUrl(mockWebServer.url("/").toString())
//                .build();
//
//        client = new AviationApiProvider(testWebClient);
//
//        // Set timeout for test
//        ReflectionTestUtils.setField(client, "timeout", 5000L);
//    }
//
//    @AfterEach
//    void tearDown() throws IOException {
//        mockWebServer.shutdown();
//    }
//
//    @Test
//    void testGetAirport_success() {
//        String json = """
//                {
//                  "KATL": [
//                    { "facility_name": "Atlanta Airport", "icao_ident": "KATL" }
//                  ]
//                }
//                """;
//
//        mockWebServer.enqueue(new MockResponse()
//                .setBody(json)
//                .addHeader("Content-Type", "application/json")
//                .setResponseCode(200));
//
//        AirportResponse response = client.getAirportByIcao("KATL");
//
//        assertNotNull(response);
//        assertEquals("Atlanta Airport",
//                response.get("KATL").get(0).getFacilityName());
//    }
//
//    @Test
//    void testGetAirport_notFound() {
//        mockWebServer.enqueue(new MockResponse().setResponseCode(404));
//
//        assertThrows(AirportNotFoundException.class,
//                () -> client.getAirportByIcao("XXXX"));
//    }
//
//    @Test
//    void testGetAirport_500Error() {
//        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
//
//        assertThrows(ExternalApiException.class,
//                () -> client.getAirportByIcao("KATL"));
//    }
//}
