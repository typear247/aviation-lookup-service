package co.za.aviationservice.service;


import co.za.aviationservice.client.AviationApiClient;
import co.za.aviationservice.model.AirportResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AviationApiClient aviationApiClient;

    @Override
    @CircuitBreaker(name = "aviationApi", fallbackMethod = "getAirportFallback")
    @Retry(name = "aviationApi")
    @RateLimiter(name = "aviationApi")
    public AirportResponse getAirportByIcao(String icaoCode) {
        log.debug("Fetching airport data for ICAO: {}", icaoCode);
        return aviationApiClient.getAirportByIcao(icaoCode);
    }

    /**
     * Fallback method when circuit breaker is open or retries exhausted
     */
    private AirportResponse getAirportFallback(String icaoCode, Exception ex) {
        log.error("Fallback triggered for ICAO: {}. Error: {}", icaoCode, ex.getMessage());

        // Return cached data or default response
        return AirportResponse.builder()
                .icao(icaoCode)
                .name("Service temporarily unavailable")
                .build();
    }
}
