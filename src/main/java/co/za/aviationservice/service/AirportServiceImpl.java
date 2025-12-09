package co.za.aviationservice.service;

import co.za.aviationservice.exception.AirportNotFoundException;
import co.za.aviationservice.model.AirportInformation;
import co.za.aviationservice.model.AirportResponse;
import co.za.aviationservice.client.ProviderBClient;
import co.za.aviationservice.utils.AviationProviderFactory;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AviationProviderFactory aviationProviderFactory;
    private final ProviderBClient providerBClient;

    /**
     * Improvements thar can be done are
     * ✔ Round-robin provider selector
     * ✔ Health-aware provider switching
     * ✔ Priority routing via config
     */


    @Override
    @CircuitBreaker(name = "aviationApi", fallbackMethod = "fallbackToProviderB")
    @Retry(name = "aviationApi")
    @RateLimiter(name = "aviationApi")
    public AirportResponse getAirportByIcao(String icaoCode) {
        log.info("Calling {} via Resilience4J for ICAO {}", aviationProviderFactory.resolve().providerName(), icaoCode);
        return aviationProviderFactory.resolve().getAirportByIcao(icaoCode);
    }

    /**
     * Triggered by Resilience4j when Provider A throws any exception.
     * Resilience4j invoked fallback.
     * Delegates to Alternative Provider B automatically.
     */
    private AirportResponse fallbackToProviderB(String icaoCode, AirportNotFoundException ex) {
        log.info("Provider A failed for {}, trying Provider B. Reason={}", icaoCode, ex.getMessage());

        try {
            return providerBClient.getAirportByIcao(icaoCode);
        } catch (Exception e) {
            return buildStaticFallbackResponse(icaoCode);
        }
    }

    private AirportResponse buildStaticFallbackResponse(String icaoCode) {
        log.info("...building  static fallback response....");
        AirportInformation info = AirportInformation.builder()
                .icaoIdent(icaoCode)
                .facilityName("Service temporarily unavailable")
                .build();

        AirportResponse response = new AirportResponse();
        response.put(icaoCode, List.of(info));

        return response;
    }
}

