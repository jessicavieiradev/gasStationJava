package com.jessicavieiradev.postoGasolina.repository;

import com.jessicavieiradev.postoGasolina.domain.entity.FuelType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FuelTypeRepository extends JpaRepository<FuelType, UUID> {
}
