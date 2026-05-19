package com.jessicavieiradev.gasStation.service;

import com.jessicavieiradev.gasStation.domain.entity.FuelPump;
import com.jessicavieiradev.gasStation.domain.entity.FuelType;
import com.jessicavieiradev.gasStation.domain.enums.Status;
import com.jessicavieiradev.gasStation.dto.fuelPumpDTO.FuelPumpRequest;
import com.jessicavieiradev.gasStation.dto.fuelPumpDTO.FuelPumpResponse;
import com.jessicavieiradev.gasStation.exception.BusinessException;
import com.jessicavieiradev.gasStation.mapper.FuelPumpMapper;
import com.jessicavieiradev.gasStation.repository.FuelPumpRepository;
import com.jessicavieiradev.gasStation.repository.FuelTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FuelPumpService implements IFuelPumpService{

    private final FuelPumpRepository fuelPumpRepository;
    private final FuelTypeRepository fuelTypeRepository;

    @Transactional
    public FuelPumpResponse create(FuelPumpRequest dto) {
        if (fuelPumpRepository.existsAnyByNameIgnoreCase(dto.name())) {
            throw new BusinessException("The fuel pump name already exists.");
        }
        FuelType fuelType = fuelTypeRepository.findById(dto.fuelTypeId())
                .orElseThrow(() -> new BusinessException("Fuel Type not found with ID: " + dto.fuelTypeId()));

        FuelPump fuelPump = FuelPumpMapper.toEntity(dto);
        fuelPump.setFuelType(fuelType);

        return FuelPumpMapper.toResponse(fuelPumpRepository.save(fuelPump));
    }

    @Transactional(readOnly = true)
    public List<FuelPumpResponse> listAll() {
        return fuelPumpRepository.findAll().stream()
                .map(FuelPumpMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FuelPumpResponse findById(UUID id) {
        return fuelPumpRepository.findById(id)
                .map(FuelPumpMapper::toResponse)
                .orElseThrow(() -> new BusinessException("Fuel Pump not found."));
    }

    @Transactional
    public FuelPumpResponse update(UUID id, FuelPumpRequest dto) {
        FuelPump fuelPump = fuelPumpRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Fuel Pump not found."));

        FuelType fuelType = fuelTypeRepository.findById(dto.fuelTypeId())
                .orElseThrow(() -> new BusinessException("Fuel Type not found."));

        fuelPump.setName(dto.name());
        fuelPump.setFuelType(fuelType);

        return FuelPumpMapper.toResponse(fuelPumpRepository.save(fuelPump));
    }

    @Transactional
    public void delete(UUID id) {
        if (!fuelPumpRepository.existsById(id)) {
            throw new BusinessException("Fuel Pump not found.");
        }
        fuelPumpRepository.deleteById(id);
    }

    @Transactional
    public void reactivate(UUID id) {
        FuelPump pump = fuelPumpRepository.findByIdIncludingInactive(id)
                .orElseThrow(() -> new BusinessException("Fuel pump not found"));

        if (pump.getStatus() == Status.ativo) {
            throw new BusinessException("This fuel pump is already active.");
        }
        if (pump.getFuelType().getStatus() == Status.inativo) {
            throw new BusinessException("It's not possible to activate this pump: the associated fuel type is inactive");
        }
        pump.setStatus(Status.ativo);
        fuelPumpRepository.save(pump);
    }
}
