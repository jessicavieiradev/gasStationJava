package com.jessicavieiradev.gasStation.repository;

import com.jessicavieiradev.gasStation.domain.entity.FuelPump;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface FuelPumpRepository extends JpaRepository<FuelPump, UUID> {
    @Query(value = """
    SELECT COUNT(*) > 0
    FROM fuel_pumps
    WHERE LOWER(name) = LOWER(?)
    """, nativeQuery = true)
    boolean existsAnyByNameIgnoreCase(String name);

    @Query(value = "SELECT * FROM fuel_pumps WHERE id = ?", nativeQuery = true)
    Optional<FuelPump> findByIdIncludingInactive(@Param("id") UUID id);
}
