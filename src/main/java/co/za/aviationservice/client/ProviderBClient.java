package co.za.aviationservice.client;

import co.za.aviationservice.exception.AirportNotFoundException;
import co.za.aviationservice.exception.ExternalApiException;
import co.za.aviationservice.model.AirportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatusCode;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProviderBClient implements AirportProvider {

    private final WebClient.Builder builder;

    @Override
    public Mono<AirportResponse> getAirportByIcao(String icaoCode) {

        WebClient webClient = builder.baseUrl("https://backup-aviation.com").build();
        log.info("Calling Provider B - Backup Aviation API");

        return webClient.get()
                .uri(uri -> uri
                        .path("/api/airport-info")
                        .queryParam("code", icaoCode)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, resp -> Mono.error(new AirportNotFoundException("Airport not found in Provider B")))
                .onStatus(HttpStatusCode::is5xxServerError, resp -> Mono.error(new ExternalApiException("Provider B unavailable")))
                .bodyToMono(AirportResponse.class);
    }
}

