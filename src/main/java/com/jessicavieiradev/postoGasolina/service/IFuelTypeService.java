package com.jessicavieiradev.postoGasolina.service;

import com.jessicavieiradev.postoGasolina.dto.fuelTypeDTO.FuelTypeRequest;
import com.jessicavieiradev.postoGasolina.dto.fuelTypeDTO.FuelTypeResponse;

import java.util.List;
import java.util.UUID;

public interface IFuelTypeService {
    FuelTypeResponse create(FuelTypeRequest dto);
    List<FuelTypeResponse> listAll();
    FuelTypeResponse findById(UUID id);
    FuelTypeResponse update(UUID id, FuelTypeRequest dto);
    public void delete(UUID id);
    void reactivate(UUID id);
}
