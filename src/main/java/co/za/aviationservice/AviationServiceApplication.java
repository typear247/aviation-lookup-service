package co.za.aviationservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Slf4j
public class AviationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AviationServiceApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void logSwaggerUrl() {
        log.info("\n----------------------------------------------------------");
        log.info("Swagger UI: http://localhost:8080/aviationlookupservice/swagger-ui.html");
        log.info("OpenAPI JSON: http://localhost:8080/aviationlookupservice/v3/api-docs");
        log.info("Health endpoint: http://localhost:8080/aviationlookupservice/actuator/health");
        log.info("Prometheus metrics: http://localhost:8080/aviationlookupservice/actuator/prometheus");
        log.info("----------------------------------------------------------\n");
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}