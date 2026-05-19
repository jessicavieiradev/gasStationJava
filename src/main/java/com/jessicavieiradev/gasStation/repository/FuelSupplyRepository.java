package com.jessicavieiradev.gasStation.repository;

import com.jessicavieiradev.gasStation.domain.entity.FuelSupply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FuelSupplyRepository extends JpaRepository<FuelSupply, UUID> {
}
