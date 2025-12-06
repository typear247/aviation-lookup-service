package co.za.aviationservice.controller;


import co.za.aviationservice.model.AirportResponse;
import co.za.aviationservice.service.AirportService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/weather")
@RequiredArgsConstructor
@Validated
public class WeatherController {

    private final AirportService airportService;

    /**
     * Get the METAR for a specified airpor by ICAO code
     *
     * @param icaoCode 4-letter ICAO code (e.g., KJFK, EGLL)
     * @return Airport details
     */
    @GetMapping("/{icaoCode}")
    public ResponseEntity<AirportResponse> getAirportByIcao(
            @PathVariable
            @Pattern(regexp = "^[A-Z]{4}$", message = "ICAO code must be 4 uppercase letters")
            String icaoCode) {

        log.info("Received request for airport with ICAO code: {}", icaoCode);
        AirportResponse response = airportService.getAirportByIcao(icaoCode);
        log.info("Successfully retrieved airport details for: {}", icaoCode);
        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Service is healthy");
    }
}
