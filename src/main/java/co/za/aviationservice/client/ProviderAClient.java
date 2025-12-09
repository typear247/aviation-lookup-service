package co.za.aviationservice.client;

import co.za.aviationservice.exception.AirportNotFoundException;
import co.za.aviationservice.exception.ExternalApiException;
import co.za.aviationservice.model.AirportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderAClient implements AirportProvider {

    @Value("${aviation.api.base-url:https://aviationapi.com}")
    private String aviationApiBaseUrl;

    @Override
    public String providerName() {
        return "providerA"; // or "aviation-api"
    }

    private final WebClient webClient;

    @Override
    public AirportResponse getAirportByIcao(String icaoCode) {
        log.info("Calling Provider-A aviation API");

        try {
            log.debug("Calling aviation API for ICAO: {}", icaoCode);

            // Call external API
            AirportResponse airportResponse = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/airports")
                            .queryParam("apt", icaoCode)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatus.NOT_FOUND::equals,
                            response -> Mono.error(new AirportNotFoundException(icaoCode)))
                    .bodyToMono(AirportResponse.class)
                    .block();

            //Todo: since API is currently returning empty 200 response for invalid/unknown Icao, will
            assert airportResponse != null;
            if (airportResponse.isEmpty() || airportResponse.get(icaoCode).isEmpty()) {
                throw new AirportNotFoundException(icaoCode);
            }

            return airportResponse;

        } catch (AirportNotFoundException ex) {
            log.warn("Airport not found for ICAO: {}", icaoCode);
            throw new AirportNotFoundException(icaoCode);

        } catch (WebClientResponseException ex) {
            log.error("API error for ICAO {}: Status={}, Body={}",
                    icaoCode, ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new ExternalApiException("Aviation API error: " + ex.getMessage(), ex);

        } catch (Exception ex) {
            log.error("Unexpected error calling aviation API for ICAO: {}", icaoCode, ex);
            throw new ExternalApiException("Failed to fetch airport data", ex);
        }
    }

}
