package com.jessicavieiradev.postoGasolina.controller;

import com.jessicavieiradev.postoGasolina.dto.fuelPumpDTO.FuelPumpRequest;
import com.jessicavieiradev.postoGasolina.dto.fuelPumpDTO.FuelPumpResponse;
import com.jessicavieiradev.postoGasolina.service.FuelPumpService;
import com.jessicavieiradev.postoGasolina.service.IFuelPumpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fuel-pumps")
@RequiredArgsConstructor
public class FuelPumpController {

    private final IFuelPumpService fuelPumpService;

    @PostMapping
    public ResponseEntity<FuelPumpResponse> create(@RequestBody @Valid FuelPumpRequest dto) {
        FuelPumpResponse response = fuelPumpService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FuelPumpResponse>> listAll() {
        return ResponseEntity.ok(fuelPumpService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuelPumpResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(fuelPumpService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuelPumpResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid FuelPumpRequest dto
    ) {
        return ResponseEntity.ok(fuelPumpService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        fuelPumpService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<Void> reactivate(@PathVariable UUID id) {
        fuelPumpService.reactivate(id);
        return ResponseEntity.noContent().build();
    }
}