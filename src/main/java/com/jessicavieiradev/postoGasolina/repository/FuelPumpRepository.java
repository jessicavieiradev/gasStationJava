package com.jessicavieiradev.postoGasolina.repository;

import com.jessicavieiradev.postoGasolina.domain.entity.FuelPump;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface FuelPumpRepository extends JpaRepository<FuelPump, UUID> {
    boolean existsByName(String name);

    @Query(value = "SELECT * FROM fuel_pumps WHERE id = ?", nativeQuery = true)
    Optional<FuelPump> findByIdIncludingInactive(@Param("id") UUID id);
}
