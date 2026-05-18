package com.jessicavieiradev.postoGasolina.repository;

import com.jessicavieiradev.postoGasolina.domain.entity.FuelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface FuelTypeRepository extends JpaRepository<FuelType, UUID> {
    boolean existsByName(String name);

    @Query(value = "SELECT * FROM fuel_types WHERE id = ?", nativeQuery = true)
    Optional<FuelType> findByIdIncludingInactive(@Param("id") UUID id);
}
