package co.za.aviationservice.controller;


import co.za.aviationservice.model.AirportResponse;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/v1/weather")
@RequiredArgsConstructor
@Validated
public class WeatherController {


    @GetMapping("/{icaoCode}")
    public ResponseEntity<AirportResponse> getWeatherByIcao(
            @PathVariable
            @Pattern(regexp = "^[A-Z]{4}$", message = "ICAO code must be 4 uppercase letters")
            String icaoCode) {

        log.info("Received request for airport with ICAO code: {}", icaoCode);
        AirportResponse response = new AirportResponse();
        response.put("uhm.... i think its raining... i dont know", new ArrayList<>());
        log.info("Successfully retrieved airport details for: {}", icaoCode);
        return ResponseEntity.ok(response);
    }
}
