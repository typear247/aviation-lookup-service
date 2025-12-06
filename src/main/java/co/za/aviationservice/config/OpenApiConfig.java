package co.za.aviationservice.config;



import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI openAPI(){
        return new OpenAPI()
                .addServersItem(new Server().url("/aviationlookupservice/"))
                .info(getInfo());
    }

    @Bean
    Info getInfo(){
        return new Info()
                .title("Aviation Lookup Service")
                .description("Spring Boot microservice for retrieving aviation information from the Aviation API (https://www.aviationapi.com/).")
                .version("1.0.0")
                .contact(new Contact().email("typear247@gmail.com").url("https://github.com/typear247/aviation-lookup-service"));
    }

    @Bean
    public GroupedOpenApi healthGroup() {
        return GroupedOpenApi.builder()
                .group("Health Controller")
                .pathsToMatch("/health/**")
                .build();
    }

    @Bean
    public GroupedOpenApi airportGroup() {
        return GroupedOpenApi.builder()
                .group("Airport Controller")
                .pathsToMatch("/v1/airports/**")
                .build();
    }

    @Bean
    public GroupedOpenApi weatherGroup() {
        return GroupedOpenApi.builder()
                .group("Weather Controller")
                .pathsToMatch("/v1/weather/**")
                .build();
    }

    @Bean
    public GroupedOpenApi chartsGroup() {
        return GroupedOpenApi.builder()
                .group("Charts Controller")
                .pathsToMatch("/v1/charts/**")
                .build();
    }


    @Bean
    public GroupedOpenApi routesGroup() {
        return GroupedOpenApi.builder()
                .group("Routes Controller")
                .pathsToMatch("/v1/routes/**")
                .build();
    }
}

