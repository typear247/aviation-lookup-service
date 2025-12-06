package co.za.aviationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class AviationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AviationServiceApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void logSwaggerUrl() {
        System.out.println("\n----------------------------------------------------------");
        System.out.println("Swagger UI: http://localhost:8080/aviationlookupservice/swagger-ui.html");
        System.out.println("OpenAPI JSON: http://localhost:8080/aviationlookupservice/v3/api-docs");
        System.out.println("Health endpoint: http://localhost:8080/aviationlookupservice/actuator/health");
        System.out.println("Prometheus metrics: http://localhost:8080/aviationlookupservice/actuator/prometheus");
        System.out.println("----------------------------------------------------------\n");
    }

}