package co.za.aviationservice.client;

import co.za.aviationservice.exception.AirportNotFoundException;
import co.za.aviationservice.exception.ExternalApiException;
import co.za.aviationservice.model.AirportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public String providerName() {
        return "providernameB";
    }

    @Override
    public AirportResponse getAirportByIcao(String icaoCode) {
        log.info("Calling Provider B - Backup Aviation API");
        throw new AirportNotFoundException("Airport not found in Provider B");
    }
}

