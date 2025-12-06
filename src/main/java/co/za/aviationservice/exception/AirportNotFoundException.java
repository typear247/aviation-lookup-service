package co.za.aviationservice.exception;


public class AirportNotFoundException extends RuntimeException {
    public AirportNotFoundException(String icaoCode) {
        super("Airport not found for ICAO code: " + icaoCode);
    }
}
