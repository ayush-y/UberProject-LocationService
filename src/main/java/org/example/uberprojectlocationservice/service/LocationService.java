package org.example.uberprojectlocationservice.service;

import org.example.uberprojectlocationservice.dto.DriverLocationDto;

import java.util.List;

public interface LocationService {

    Boolean saveDriverLocation(String driverId, Double latitude, Double Longitude);

    List<DriverLocationDto> getNearByDrivers(Double latitude, Double Longitude);

}
