package com.jessicavieiradev.postoGasolina.mapper;

import com.jessicavieiradev.postoGasolina.domain.entity.FuelPump;
import com.jessicavieiradev.postoGasolina.dto.fuelPumpDTO.FuelPumpRequest;
import com.jessicavieiradev.postoGasolina.dto.fuelPumpDTO.FuelPumpResponse;

public class FuelPumpMapper {
    public static FuelPump toEntity(FuelPumpRequest dto) {
        FuelPump fuelPump = new FuelPump();
        fuelPump.setName(dto.name());
        return fuelPump;
    }

    public static FuelPumpResponse toResponse(FuelPump entity) {
        return new FuelPumpResponse(
                entity.getId(),
                entity.getName(),
                entity.getStatus().toString(),
                entity.getCreatedAt(),
                entity.getFuelType().getId(),
                entity.getFuelType().getName()
        );
    }
}
