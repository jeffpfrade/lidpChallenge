package com.lidp.fare.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "fare")
public class Fare
{
   @EmbeddedId
   private FareId fareId;
   @Column
   private double cost;

   public Fare() {
      // Default constructor
   }

   public Fare (FareId fareId, double cost) {
      this.fareId = fareId;
      this.cost = cost;
   }

   public Instant getDepartureTime()
   {
      return this.fareId.getDepartureTime();
   }

   public double getDistanceMi()
   {
      return fareId.getDistanceMi();
   }

   public int getSeatRow()
   {
      return fareId.getSeatRow();
   }

   public double getCost()
   {
      return cost;
   }

   public FareId getFareId(){
      return fareId;
   }

   public void setCost(double cost)
   {
      this.cost = cost;
   }

   @Override
   public String toString()
   {
      return "Fare{" + "departureTime=" + fareId.getDepartureTime() + ", distanceMi=" + fareId.getDistanceMi() + ", seatRow=" + fareId.getSeatRow() + ", cost=" + cost + '}';
   }
}
