package com.jessicavieiradev.postoGasolina.service;

import com.jessicavieiradev.postoGasolina.domain.entity.FuelType;
import com.jessicavieiradev.postoGasolina.dto.fuelTypeDTO.FuelTypeRequest;
import com.jessicavieiradev.postoGasolina.dto.fuelTypeDTO.FuelTypeResponse;
import com.jessicavieiradev.postoGasolina.exception.BusinessException;
import com.jessicavieiradev.postoGasolina.mapper.FuelTypeMapper;
import com.jessicavieiradev.postoGasolina.repository.FuelTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FuelTypeService {

    private final FuelTypeRepository fuelTypeRepository;

    @Transactional
    public FuelTypeResponse createFuelType(FuelTypeRequest dto) {
        if (fuelTypeRepository.existsByName(dto.name())) {
            throw new BusinessException("Fuel type already exists.");
        }

        FuelType fuelType = FuelTypeMapper.toEntity(dto);
        fuelType = fuelTypeRepository.save(fuelType);

        return FuelTypeMapper.toResponse(fuelType);
    }

    @Transactional(readOnly = true)
    public List<FuelTypeResponse> listAll() {
        return fuelTypeRepository.findAll()
                .stream()
                .map(FuelTypeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FuelTypeResponse findById(UUID id) {
        FuelType fuelType = fuelTypeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Fuel type not found."));
        return FuelTypeMapper.toResponse(fuelType);
    }

    @Transactional
    public FuelTypeResponse updateFuelType(UUID id, FuelTypeRequest dto) {
        FuelType fuelType = fuelTypeRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Fuel type not found."));

        if (fuelTypeRepository.existsByName(dto.name()) && !fuelType.getName().equals(dto.name())) {
            throw new BusinessException("Fuel type name already in use.");
        }

        fuelType.setName(dto.name());
        fuelType.setPrice(dto.price());

        return FuelTypeMapper.toResponse(fuelTypeRepository.save(fuelType));
    }

    @Transactional
    public void deleteFuelType(UUID id) {
        if (!fuelTypeRepository.existsById(id)) {
            throw new BusinessException("Fuel type not found.");
        }
        fuelTypeRepository.deleteById(id);
    }
}