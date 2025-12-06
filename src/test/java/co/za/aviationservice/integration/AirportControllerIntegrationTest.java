package co.za.aviationservice.integration;

import co.za.aviationservice.model.AirportResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AirportControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static MockWebServer mockWebServer;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        registry.add("aviation.api.base-url",
                () -> mockWebServer.url("/").toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void shouldReturnAirportDetails_WhenValidIcaoCode() {
        // Given
        String icaoCode = "KJFK";
        String mockResponse = """
                {
                    "icaoCode": "KJFK",
                    "iataCode": "JFK",
                    "name": "John F Kennedy International Airport",
                    "city": "New York",
                    "country": "United States",
                    "location": {
                        "latitude": 40.6398,
                        "longitude": -73.7789
                    },
                    "elevation": 13,
                    "timezone": "America/New_York"
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(mockResponse)
                .addHeader("Content-Type", "application/json"));

        // When
        ResponseEntity<AirportResponse> response = restTemplate.getForEntity(
                "/api/v1/airports/" + icaoCode,
                AirportResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getIcaoCode()).isEqualTo("KJFK");
        assertThat(response.getBody().getName())
                .isEqualTo("John F Kennedy International Airport");
    }

    @Test
    void shouldReturnNotFound_WhenInvalidIcaoCode() {
        // Given
        String icaoCode = "XXXX";
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/v1/airports/" + icaoCode,
                String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnBadRequest_WhenInvalidIcaoFormat() {
        // Given
        String invalidIcaoCode = "ABC"; // Less than 4 characters

        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/v1/airports/" + invalidIcaoCode,
                String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldHandleTimeout_Gracefully() {
        // Given
        String icaoCode = "KJFK";
        mockWebServer.enqueue(new MockResponse()
                .setBody("{}")
                .setBodyDelay(10, java.util.concurrent.TimeUnit.SECONDS));

        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/v1/airports/" + icaoCode,
                String.class);

        // Then
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }
}