package co.za.aviationservice.utils;


import co.za.aviationservice.service.AirportService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AviationProviderFactory {

    private final List<AirportService> providers;

    @Value("${aviation.provider}")
    private String aviationProviderName;

    public AviationProviderFactory(List<AirportService> providers) {
        this.providers = providers;
    }

    public AirportService resolve() {
        return providers.stream()
                .filter(p -> p.getClass().getSimpleName().toLowerCase().contains(aviationProviderName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No aviation provider found"));
    }

}
