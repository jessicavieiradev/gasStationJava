package com.jessicavieiradev.postoGasolina.service;

import com.jessicavieiradev.postoGasolina.dto.fuelPumpDTO.FuelPumpRequest;
import com.jessicavieiradev.postoGasolina.dto.fuelPumpDTO.FuelPumpResponse;

import java.util.List;
import java.util.UUID;

public interface IFuelPumpService {
    FuelPumpResponse create(FuelPumpRequest dto);
    List<FuelPumpResponse> listAll();
    FuelPumpResponse findById(UUID id);
    FuelPumpResponse update(UUID id, FuelPumpRequest dto);
    void delete(UUID id);
    void reactivate(UUID id);
}
