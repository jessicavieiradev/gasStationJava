package com.jessicavieiradev.gasStation.dto.FuelSupplyDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record FuelSupplyResponse(
        UUID id,
        LocalDateTime date,
        String fuelTypeName,
        String fuelPumpName,
        BigDecimal totalAmount,
        BigDecimal literage,
        UUID fuelPumpId,
        String status,
        LocalDateTime createdAt
) {
}
