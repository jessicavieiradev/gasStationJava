package com.jessicavieiradev.gasStation.dto.fuelPumpDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record FuelPumpResponse(
        UUID id,
        String name,
        String status,
        LocalDateTime createdAt,
        UUID fuelTypeId,
        String fuelTypeName
) {}
