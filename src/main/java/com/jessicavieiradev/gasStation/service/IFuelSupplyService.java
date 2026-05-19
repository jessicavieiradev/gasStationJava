package com.jessicavieiradev.gasStation.service;

import com.jessicavieiradev.gasStation.dto.FuelSupplyDTO.FuelSupplyRequest;
import com.jessicavieiradev.gasStation.dto.FuelSupplyDTO.FuelSupplyResponse;

import java.util.List;
import java.util.UUID;

public interface IFuelSupplyService {
    FuelSupplyResponse create(FuelSupplyRequest dto);
    List<FuelSupplyResponse> listAll();
    FuelSupplyResponse findById(UUID id);
    void delete(UUID id);
}
