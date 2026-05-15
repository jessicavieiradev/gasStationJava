package com.jessicavieiradev.postoGasolina.repository;

import com.jessicavieiradev.postoGasolina.domain.entity.FuelPump;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FuelPumpRepository extends CrudRepository<FuelPump, UUID> {
}
