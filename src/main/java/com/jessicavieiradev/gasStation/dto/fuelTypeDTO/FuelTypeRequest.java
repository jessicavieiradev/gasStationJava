package com.jessicavieiradev.gasStation.dto.fuelTypeDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record FuelTypeRequest(@NotBlank @Size(max = 200) String name, @NotNull @Positive BigDecimal price) {
}
