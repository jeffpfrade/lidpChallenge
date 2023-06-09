package com.lidp.fare.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import com.lidp.fare.dao.FareRepository;
import com.lidp.fare.domain.Fare;
import com.lidp.fare.domain.FareId;
import org.springframework.stereotype.Service;

@Service("fareService")
public class FareService
{
   private static final Logger logger = Logger.getLogger(FareService.class.getName());

   private final FareRepository fareRepository;

   public FareService(FareRepository fareRepository)
   {
      this.fareRepository = fareRepository;
   }

   public List<Fare> getFares()
   {
      return fareRepository.findAll();
   }

   public Optional<Fare> findFareById(FareId id) {
      return fareRepository.findById(id);
   }

   public Fare saveFare(FareId fareId) {

      Optional<Fare> result = fareRepository.findById(fareId);
      // check if fare has already been calculated
      if (result.isPresent()) {
         // if it exists we do not save it, just return it
         Fare fare = result.get();
         logger.info("Returning existing fare: " + fare);
         return fare;
      }
      //Calculate cost
      double fareCost = calculateFare(fareId.getDepartureTime(), fareId.getDistanceMi(), fareId.getSeatRow());
      // Save new Fare
      Fare newFare = new Fare(fareId, fareCost);
      return fareRepository.save(newFare);
   }

   public double getFare(Instant departureTime, double distanceMi, int seatRow)
   {
      // check if fare has already been calculated
      FareId id = new FareId(departureTime, distanceMi, seatRow);
      Optional<Fare> result = fareRepository.findById(id);

      if (result.isPresent())
      {
         // if cost calculation exists, return it
         Fare fare = result.get();
         logger.info("Returning existing fare: " + fare);
         return fare.getCost();
      }
      else
      {
         // else calculate it and persist the result
         double cost = calculateFare(departureTime, distanceMi, seatRow);

         Fare fare = new Fare(id, cost);
         fareRepository.save(fare);

         logger.info("Saving new fare: " + fare);

         return cost;
      }
   }

   private double calculateFare(Instant departureTime, double distanceMi, int seatRow)
   {
      // the higher the service level (based on the seat row), the higher the base rate
      double baseRate = getBaseRate(seatRow);

      // the greater the distance, the higher the fare
      double distanceFee = distanceMi * 0.1;

      // the closer to the departure date, the higher the fare
      long daysUntilDeparture = Instant.now().until(departureTime, ChronoUnit.DAYS);
      double departureTimeFee = (0.0088 * daysUntilDeparture * daysUntilDeparture) - (1.3869 * daysUntilDeparture) + 100;

      return baseRate + distanceFee + departureTimeFee;
   }

   private double getBaseRate(int seatPosition)
   {
      if (seatPosition < 4)
      {
         // first class base rate
         return 300;
      }
      else if (seatPosition < 8)
      {
         // comfort base rate
         return 150;
      }
      else
      {
         // economy base rate
         return 50;
      }
   }
}
