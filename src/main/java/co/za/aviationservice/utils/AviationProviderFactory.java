package co.za.aviationservice.utils;


import co.za.aviationservice.client.AirportProvider;
import co.za.aviationservice.service.AirportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AviationProviderFactory {

    private final List<AirportProvider> providers;

    @Value("${aviation.provider}")
    private String aviationProviderName;

    public AviationProviderFactory(List<AirportProvider> providers) {
        this.providers = providers;
    }

    public AirportProvider resolve() {
        return providers.stream()
                .filter(p -> p.providerName().equalsIgnoreCase(aviationProviderName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No aviation provider found for name: " + aviationProviderName));
    }

}
