package com.lidp.fare.controller;

import com.lidp.fare.domain.Fare;
import com.lidp.fare.domain.FareId;
import com.lidp.fare.service.FareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

@RestController
public class FareController {

    @Autowired
    private FareService fareService;

    // Save operation
    @PostMapping("/fares")
    public Fare saveFare(@RequestBody FareId fareId){
        return fareService.saveFare(fareId);
    }

    // Read
    @GetMapping("/fares")
    public Iterable<Fare> getAllFares(){
        return fareService.getFares();
    }

    // Get by id
    @GetMapping("/fares/getById")
    public Optional<Fare> getFareById(@RequestBody FareId fareId){
        return fareService.findFareById(fareId);
    }

    // Get fare by params
    @GetMapping("/fares/{departureTime}/{distanceMi}/{seatRow}")
    public double getFare(
            @PathVariable Instant departureTime,
            @PathVariable double distanceMi,
            @PathVariable int seatRow ) {
        return this.fareService.getFare(departureTime, distanceMi, seatRow);
    }
}
