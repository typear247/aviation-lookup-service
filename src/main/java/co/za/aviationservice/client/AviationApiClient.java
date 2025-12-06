package co.za.aviationservice.client;


import co.za.aviationservice.model.AirportInformation;
import co.za.aviationservice.model.AirportResponse;

public interface AviationApiClient {
    AirportResponse getAirportByIcao(String icaoCode);
}
