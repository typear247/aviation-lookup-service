package co.za.aviationservice.service;


import co.za.aviationservice.model.AirportResponse;

/**
 * Notes : SERVICE CONTRACT (Interface) / PROVIDER ABSTRACTION — decoupling API vendors
 *          ✔ Multiple API vendors
 *          ✔ Easy switching
 *          ✔ Fallback routing logic
 * */


public interface AirportService {
    AirportResponse getAirportByIcao(String icaoCode);
}
