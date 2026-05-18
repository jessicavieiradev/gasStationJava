package com.jessicavieiradev.postoGasolina.mapper;

import com.jessicavieiradev.postoGasolina.domain.entity.FuelSupply;
import com.jessicavieiradev.postoGasolina.dto.FuelSupplyDTO.FuelSupplyRequest;
import com.jessicavieiradev.postoGasolina.dto.FuelSupplyDTO.FuelSupplyResponse;

public class FuelSupplyMapper {
    public static FuelSupply toEntity(FuelSupplyRequest dto) {
        return new FuelSupply();
    }

    public static FuelSupplyResponse toResponse(FuelSupply entity) {
        return new FuelSupplyResponse(
                entity.getId(),
                entity.getDate(),
                entity.getFuelPump().getFuelType().getName(),
                entity.getFuelPump().getName(),
                entity.getTotalAmount(),
                entity.getLiterage(),
                entity.getFuelPump().getId(),
                entity.getStatus().toString(),
                entity.getCreatedAt()
        );
    }
}