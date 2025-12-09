package co.za.aviationservice.controller;




import co.za.aviationservice.model.AirportResponse;
import co.za.aviationservice.model.ErrorResponse;
import co.za.aviationservice.service.AirportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Pattern;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/v1/airports")
@RequiredArgsConstructor
@Validated
public class AirportController {

    private final AirportService airportService;

    /** Notes
     * Stateless : stateless controller does not store any client-specific data or session information
     * Stateful : controller maintains information about the client's session or previous interactions on the server-side
     * */

    /**
     * Retrieve airport details by ICAO code.
     * Airport data sourced from Aviation API:
     * https://docs.aviationapi.com/#tag/airports
     */
    @Operation(
            summary = "Get airport details by ICAO code",
            description = """
                    Retrieves airport metadata including airport name, location, latitude/longitude, 
                    elevation, and timezone based on a 4-letter ICAO code.

                    Example ICAO codes:
                     • KATL — Hartsfield-Jackson Atlanta International Airport
                     • KJFK — John F. Kennedy Airport
                     • EGLL — London Heathrow Airport
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Airport found", content = @Content(schema = @Schema(implementation = AirportResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid ICAO format. Must be 4 uppercase letters.", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Airport not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Unexpected server error or external API failure", content = @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json"))
    })
    @GetMapping("/{icaoCode}")
    public ResponseEntity<AirportResponse> getAirportByIcao(
            @Parameter(description = "4-letter ICAO airport code (uppercase letters only)", example = "KATL", required = true)
            @PathVariable
            @Pattern(regexp = "^[A-Z]{4}$", message = "ICAO code must be 4 uppercase letters") String icaoCode) {
        final long startTime = System.currentTimeMillis();
        log.info("Received request for airport with ICAO code: {}", icaoCode);
        AirportResponse response = airportService.getAirportByIcao(icaoCode);
        log.info("Successfully retrieved airport details for : {} in {}ms", icaoCode, System.currentTimeMillis() - startTime);

        return ResponseEntity.ok(response);
    }


}
