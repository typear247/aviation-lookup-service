package co.za.aviationservice.client;

import co.za.aviationservice.exception.AirportNotFoundException;
import co.za.aviationservice.exception.ProviderUnavailableException;
import co.za.aviationservice.model.AirportResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatusCode;
import reactor.netty.http.client.PrematureCloseException;

import java.net.UnknownHostException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderAClient implements AirportProvider {

    @Value("${aviation.api.base-url:https://aviationapi.com}")
    private String aviationApiBaseUrl;

    @Override
    public String providerName() {
        return "providernameA"; // or "aviation-api"
    }

    private final WebClient.Builder builder;


    @Override
    public Mono<AirportResponse> getAirportByIcao(String icaoCode) {
        log.info("Calling Provider A - Aviation API");

        WebClient webClient = builder.baseUrl(aviationApiBaseUrl).build();

        return webClient.get()
                .uri(uri -> uri.path("/v1/airports").queryParam("apt", icaoCode).build())
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError,
                        resp -> Mono.error(new ProviderUnavailableException("Provider A unavailable")))
                .bodyToMono(AirportResponse.class)
                .onErrorMap(e -> new ProviderUnavailableException("Provider unavailable: " + e.getMessage()));
    }

}
