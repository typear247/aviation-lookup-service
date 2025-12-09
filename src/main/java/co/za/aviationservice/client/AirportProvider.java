package co.za.aviationservice.client;


import co.za.aviationservice.model.AirportResponse;


//Notes:Provider Abstraction (Extensible Design)
//implementations get plugged in without changing business logic

// ✔ API Provider A [aviationapi]
//✔ API Provider B [AdolphAviationapi ]
//✔ Mock provider (for testing / fallback)


public interface AirportProvider {

    String providerName();

    AirportResponse getAirportByIcao(String icaoCode);
}
