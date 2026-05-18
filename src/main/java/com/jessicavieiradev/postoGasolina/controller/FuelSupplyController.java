package com.jessicavieiradev.postoGasolina.controller;

import com.jessicavieiradev.postoGasolina.dto.FuelSupplyDTO.FuelSupplyRequest;
import com.jessicavieiradev.postoGasolina.dto.FuelSupplyDTO.FuelSupplyResponse;
import com.jessicavieiradev.postoGasolina.dto.fuelPumpDTO.FuelPumpRequest;
import com.jessicavieiradev.postoGasolina.dto.fuelPumpDTO.FuelPumpResponse;
import com.jessicavieiradev.postoGasolina.repository.FuelSupplyRepository;
import com.jessicavieiradev.postoGasolina.service.FuelSupplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fuel-supplies")
@RequiredArgsConstructor
public class FuelSupplyController {
    private final FuelSupplyService fuelSupplyService;

    @PostMapping
    public ResponseEntity<FuelSupplyResponse> create(@RequestBody @Valid FuelSupplyRequest dto) {
        FuelSupplyResponse response = fuelSupplyService.createFuelSupply(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FuelSupplyResponse>> listAll() {
        return ResponseEntity.ok(fuelSupplyService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuelSupplyResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(fuelSupplyService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        fuelSupplyService.deleteFuelSupply(id);
        return ResponseEntity.noContent().build();
    }
}
