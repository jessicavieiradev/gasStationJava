package com.jessicavieiradev.postoGasolina.repository;

import com.jessicavieiradev.postoGasolina.entity.FuelSupply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FuelSupplyRepository extends JpaRepository<FuelSupply, UUID> {
}
