package com.jessicavieiradev.postoGasolina.repository;

import com.jessicavieiradev.postoGasolina.domain.entity.FuelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FuelTypeRepository extends JpaRepository<FuelType, UUID> {
    boolean existsByName(String name);
}
