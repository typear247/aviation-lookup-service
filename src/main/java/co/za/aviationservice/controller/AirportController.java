package co.za.aviationservice.controller;



import co.za.aviationservice.model.AirportResponse;
import co.za.aviationservice.service.AirportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Pattern;

@Slf4j
@RestController
@RequestMapping("/api/v1/airports")
@RequiredArgsConstructor
@Validated
public class AirportController {

    private final AirportService airportService;

    /**
     * Retrieve airport details by ICAO code
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
