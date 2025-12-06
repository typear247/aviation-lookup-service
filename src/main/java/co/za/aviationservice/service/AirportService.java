package co.za.aviationservice.service;


import co.za.aviationservice.model.AirportResponse;

public interface AirportService {
    AirportResponse getAirportByIcao(String icaoCode);
}
