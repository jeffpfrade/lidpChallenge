package com.lidp.fare.dao;

import java.util.List;
import java.util.Optional;

import com.lidp.fare.domain.Fare;
import com.lidp.fare.domain.FareId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FareRepository extends JpaRepository<Fare, FareId>
{
   List<Fare> findAll();
   Optional<Fare> findById(FareId fareId);
   Fare save(Fare fare);
}
