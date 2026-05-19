package com.jessicavieiradev.gasStation.dto.fuelTypeDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record FuelTypeResponse(UUID id, String name, BigDecimal price, String status, LocalDateTime createdAt) {
}
