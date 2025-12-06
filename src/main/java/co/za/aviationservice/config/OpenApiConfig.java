package co.za.aviationservice.config;



import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI airportOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Airport Lookup API")
                        .description("Microservice to fetch airport information by ICAO code")
                        .version("1.0.0"));
    }
}

