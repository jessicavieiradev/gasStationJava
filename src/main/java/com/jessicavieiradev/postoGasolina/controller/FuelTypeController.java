package com.jessicavieiradev.postoGasolina.controller;

import com.jessicavieiradev.postoGasolina.dto.fuelTypeDTO.FuelTypeRequest;
import com.jessicavieiradev.postoGasolina.dto.fuelTypeDTO.FuelTypeResponse;
import com.jessicavieiradev.postoGasolina.service.IFuelTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fuel-types")
@RequiredArgsConstructor
public class FuelTypeController {

    private final IFuelTypeService fuelTypeService;

    @PostMapping
    public ResponseEntity<FuelTypeResponse> create(@RequestBody @Valid FuelTypeRequest dto) {
        FuelTypeResponse response = fuelTypeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FuelTypeResponse>> listAll() {
        return ResponseEntity.ok(fuelTypeService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuelTypeResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(fuelTypeService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuelTypeResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid FuelTypeRequest dto
    ) {
        return ResponseEntity.ok(fuelTypeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        fuelTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<Void> reactivate(@PathVariable UUID id) {
        fuelTypeService.reactivate(id);
        return ResponseEntity.noContent().build();
    }
}