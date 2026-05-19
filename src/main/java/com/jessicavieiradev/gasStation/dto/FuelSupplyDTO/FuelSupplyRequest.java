package com.jessicavieiradev.gasStation.dto.FuelSupplyDTO;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record FuelSupplyRequest(
        BigDecimal totalAmount,
        BigDecimal literage,
        @NotNull UUID fuelPumpId,
        LocalDateTime date
) {}