package com.jessicavieiradev.postoGasolina.dto.fuelPumpDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record FuelPumpRequest(
        @NotBlank
        @Size(max = 200)
        String name,
        @NotNull
        UUID fuelTypeId
) {}
