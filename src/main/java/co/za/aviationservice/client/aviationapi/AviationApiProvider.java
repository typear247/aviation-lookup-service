package co.za.aviationservice.client.aviationapi;



import co.za.aviationservice.client.AirportProvider;
import co.za.aviationservice.exception.AirportNotFoundException;
import co.za.aviationservice.model.AirportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AviationApiProvider implements AirportProvider {

    private final WebClient.Builder builder;

    @Value("${aviation.api.timeout:5000}")
    private long timeout;

//    @Override
//    public AirportResponse getAirportByIcao(String icaoCode) {
//        try {
//            log.debug("Calling aviation API for ICAO: {}", icaoCode);
//
//            // Call external API
//            AirportResponse airportResponse = aviationWebClient.get()
//                    .uri(uriBuilder -> uriBuilder
//                            .path("/v1/airports")
//                            .queryParam("apt", icaoCode)
//                            .build())
//                    .retrieve()
//                    .onStatus(HttpStatus.NOT_FOUND::equals,
//                            response -> Mono.error(new AirportNotFoundException(icaoCode)))
//                    .bodyToMono(AirportResponse.class)
//                    .timeout(Duration.ofMillis(timeout))
//                    .block();
//
//            //Todo: since API is currently returning empty 200 response for invalid/unknown ICAO, will add an extra check:
//            if (airportResponse.isEmpty() || airportResponse.get(icaoCode).isEmpty()) {
//                throw new AirportNotFoundException(icaoCode);
//            }
//
//            return airportResponse;
//
//        } catch (AirportNotFoundException ex) {
//            log.warn("Airport not found for ICAO: {}", icaoCode);
//            throw new AirportNotFoundException(icaoCode);
//
//        } catch (WebClientResponseException ex) {
//            log.error("API error for ICAO {}: Status={}, Body={}",
//                    icaoCode, ex.getStatusCode(), ex.getResponseBodyAsString());
//            throw new ExternalApiException("Aviation API error: " + ex.getMessage(), ex);
//
//        } catch (Exception ex) {
//            log.error("Unexpected error calling aviation API for ICAO: {}", icaoCode, ex);
//            throw new ExternalApiException("Failed to fetch airport data", ex);
//        }
//    }

    @Override
    public Mono<AirportResponse> getAirportByIcao(String icaoCode) {

        WebClient webClient = builder.baseUrl("https://api.aviationapi.com").build();
        log.debug("Calling Aviation API for ICAO {}", icaoCode);

        return webClient.get()
                .uri(uri -> uri
                        .path("/v1/airports")
                        .queryParam("apt", icaoCode)
                        .build())
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        resp -> Mono.error(new AirportNotFoundException(icaoCode)))
                .bodyToMono(AirportResponse.class)
                .flatMap(response -> {

                    // Defensive validation – API often returns empty with 200
                    if (response == null
                            || response.isEmpty()
                            || response.get(icaoCode) == null
                            || response.get(icaoCode).isEmpty()) {

                        log.warn("Received empty airport response for {}", icaoCode);
                        return Mono.error(new AirportNotFoundException(icaoCode));
                    }

                    return Mono.just(response);
                });
    }

}