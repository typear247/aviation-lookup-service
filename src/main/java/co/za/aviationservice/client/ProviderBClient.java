package co.za.aviationservice.client;

import co.za.aviationservice.exception.AirportNotFoundException;
import co.za.aviationservice.model.AirportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProviderBClient implements AirportProvider {

    @Override
    public String providerName() {
        return "providerB";
    }

    @Value("${aviation.b.api.base-url:https://aviationapi.com}")
    private String alternativeAviation;

    private final WebClient.Builder builder;

    @Override
    public AirportResponse getAirportByIcao(String icaoCode) {
        log.info("Calling Provider-B aviation API");
        throw new AirportNotFoundException("Airport not found in Provider B");

//        WebClient webClient = builder.baseUrl(alternativeAviation).build();
//
//        // Call external API
//        AirportResponse airportResponse = webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/v1/airports")
//                        .queryParam("apt", icaoCode)
//                        .build())
//                .retrieve()
//                .onStatus(HttpStatus.NOT_FOUND::equals,
//                        response -> Mono.error(new AirportNotFoundException(icaoCode)))
//                .bodyToMono(AirportResponse.class)
//                .block();
//        return  airportResponse;
    }
}

