package com.jessicavieiradev.gasStation.mapper;

import com.jessicavieiradev.gasStation.domain.entity.FuelType;
import com.jessicavieiradev.gasStation.dto.fuelTypeDTO.FuelTypeRequest;
import com.jessicavieiradev.gasStation.dto.fuelTypeDTO.FuelTypeResponse;

public class FuelTypeMapper {
    public static FuelType toEntity(FuelTypeRequest fuelTypeRequest) {
        FuelType fuelType = new FuelType();
        fuelType.setName(fuelTypeRequest.name());
        fuelType.setPrice(fuelTypeRequest.price());
        return fuelType;
    }
    public static FuelTypeResponse toResponse(FuelType fuelType) {
        return new FuelTypeResponse(
                fuelType.getId(),
                fuelType.getName(),
                fuelType.getPrice(),
                fuelType.getStatus().toString(),
                fuelType.getCreatedAt()
        );
    }
}
