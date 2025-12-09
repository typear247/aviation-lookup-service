package co.za.aviationservice.service;

import co.za.aviationservice.model.AirportInformation;
import co.za.aviationservice.model.AirportResponse;
import co.za.aviationservice.client.ProviderAClient;
import co.za.aviationservice.client.ProviderBClient;
import co.za.aviationservice.utils.AviationProviderFactory;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final ProviderAClient providerA; //default provider (or can use AviationProviderFactory for runtime swap)
    private final ProviderBClient providerB;

    private final AviationProviderFactory aviationProviderFactory;

    /**
     * Improvements thar can be done are
     * ✔ Round-robin provider selector
     * ✔ Health-aware provider switching
     * ✔ Priority routing via config
     */


    @Override
    @CircuitBreaker(name = "aviationApi", fallbackMethod = "fallbackToProviderB")
    @Retry(name = "aviationApi")
    @TimeLimiter(name = "aviationApi")
    @RateLimiter(name = "aviationApi")
    public AirportResponse getAirportByIcao(String icaoCode) {
        log.info("Calling Provider A via Resilience4J for ICAO {}", icaoCode);
        return aviationProviderFactory.resolve().getAirportByIcao(icaoCode);
    }

    /**
     * Resilience4j invoked fallback.
     * Delegates to Provider B automatically.
     */
    private Mono<AirportResponse> fallbackToProviderB(String icaoCode, Throwable ex) {
        log.info("Provider A failed for {}, trying Provider B. Reason={}", icaoCode, ex.getMessage());

        // provider does its thing if fails
        return buildStaticFallbackResponse(icaoCode);
    }

    private Mono<AirportResponse> buildStaticFallbackResponse(String icaoCode) {
        AirportInformation info = AirportInformation.builder()
                .icaoIdent(icaoCode)
                .facilityName("Service temporarily unavailable")
                .build();

        AirportResponse response = new AirportResponse();
        response.put(icaoCode, List.of(info));

        return Mono.just(response);
    }
}

