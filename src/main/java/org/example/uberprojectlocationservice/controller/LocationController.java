package org.example.uberprojectlocationservice.controller;



import org.example.uberprojectlocationservice.dto.NearbyDriversRequestDto;
import org.example.uberprojectlocationservice.dto.SaveDriverLocationRequestDto;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private StringRedisTemplate stringRedisTemplate;

    private static final String DRIVER_GEO_OPS_KEY = "drivers";

    private static final Double SEARCH_RADIUS = 5.0;



    public LocationController(StringRedisTemplate stringRedisTemplate) {

        this.stringRedisTemplate = stringRedisTemplate;

    }

    @PostMapping("/drivers")
    public ResponseEntity<Boolean> saveDriverLocation(@RequestBody SaveDriverLocationRequestDto saveDriverLocationRequestDto) {

        try {
            System.out.println("Inside saveDriverLocation");
            GeoOperations<String, String> geoOps = stringRedisTemplate.opsForGeo();
            System.out.println(stringRedisTemplate.getConnectionFactory().getConnection().info("server"));

            geoOps.add(
                    DRIVER_GEO_OPS_KEY,
                    new RedisGeoCommands.GeoLocation<>(
                            saveDriverLocationRequestDto.getDriverId(),
                            new Point
                                    (saveDriverLocationRequestDto.getLatitude(),
                                            saveDriverLocationRequestDto.getLongitude())));
            return new ResponseEntity<>(true, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/nearby/drivers")
    public ResponseEntity<List<String>> getNearbyDrivers(@RequestBody NearbyDriversRequestDto nearbyDriversRequestDto) {
        try {
            System.out.println("Inside nearByDriverLocation");
            GeoOperations<String, String> geoOps = stringRedisTemplate.opsForGeo();
            Distance radius = new Distance(SEARCH_RADIUS, Metrics.KILOMETERS);
            Circle within = new Circle(new Point(nearbyDriversRequestDto.getLatitude(), nearbyDriversRequestDto.getLongitude()), radius);

            GeoResults<RedisGeoCommands.GeoLocation<String>> results = geoOps.radius(DRIVER_GEO_OPS_KEY, within);
            List<String> drivers = new ArrayList<>();

            for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : results) {
                drivers.add(result.getContent().getName());
            }
           return new ResponseEntity<>(drivers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }
}