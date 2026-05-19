package com.jessicavieiradev.gasStation.controller;

import com.jessicavieiradev.gasStation.dto.FuelSupplyDTO.FuelSupplyRequest;
import com.jessicavieiradev.gasStation.dto.FuelSupplyDTO.FuelSupplyResponse;
import com.jessicavieiradev.gasStation.service.IFuelSupplyService;
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
    private final IFuelSupplyService fuelSupplyService;

    @PostMapping
    public ResponseEntity<FuelSupplyResponse> create(@RequestBody @Valid FuelSupplyRequest dto) {
        FuelSupplyResponse response = fuelSupplyService.create(dto);
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
        fuelSupplyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
