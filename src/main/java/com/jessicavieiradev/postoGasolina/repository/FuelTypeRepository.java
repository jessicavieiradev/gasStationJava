package com.jessicavieiradev.postoGasolina.repository;

import com.jessicavieiradev.postoGasolina.domain.entity.FuelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface FuelTypeRepository extends JpaRepository<FuelType, UUID> {
    @Query(value = """
    SELECT COUNT(*) > 0
    FROM fuel_types
    WHERE LOWER(name) = LOWER(?)
    """, nativeQuery = true)
    boolean existsAnyByNameIgnoreCase(String name);

    @Query(value = "SELECT * FROM fuel_types WHERE id = :id", nativeQuery = true)
    Optional<FuelType> findByIdIncludingInactive(@Param("id") UUID id);
}
