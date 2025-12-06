package co.za.aviationservice.model;

import lombok.Data;

import java.util.HashMap;
import java.util.List;


@Data
public class AirportResponse extends HashMap<String, List<AirportInformation>> { }