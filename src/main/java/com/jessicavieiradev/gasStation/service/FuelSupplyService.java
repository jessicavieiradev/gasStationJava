package com.jessicavieiradev.gasStation.service;

import com.jessicavieiradev.gasStation.domain.entity.FuelPump;
import com.jessicavieiradev.gasStation.domain.entity.FuelSupply;
import com.jessicavieiradev.gasStation.dto.FuelSupplyDTO.FuelSupplyRequest;
import com.jessicavieiradev.gasStation.dto.FuelSupplyDTO.FuelSupplyResponse;
import com.jessicavieiradev.gasStation.exception.BusinessException;
import com.jessicavieiradev.gasStation.mapper.FuelSupplyMapper;
import com.jessicavieiradev.gasStation.repository.FuelPumpRepository;
import com.jessicavieiradev.gasStation.repository.FuelSupplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FuelSupplyService implements IFuelSupplyService {
    private final FuelSupplyRepository fuelSupplyRepository;
    private final FuelPumpRepository fuelPumpRepository;

    @Transactional
    public FuelSupplyResponse create(FuelSupplyRequest dto) {
        FuelPump fuelPump = fuelPumpRepository.findById(dto.fuelPumpId())
                .orElseThrow(() -> new BusinessException("Fuel Pump not found or is inactive."));
        BigDecimal price = fuelPump.getFuelType().getPrice();
        BigDecimal totalAmount;
        BigDecimal literageValue;
        if(dto.literage() != null && dto.totalAmount() == null){
            literageValue = dto.literage();
            totalAmount = price.multiply(literageValue)
                    .setScale(3, java.math.RoundingMode.HALF_UP);
        }
        else if(dto.literage() == null && dto.totalAmount() != null){
            totalAmount = dto.totalAmount();
            literageValue = totalAmount.divide(price, 3, java.math.RoundingMode.HALF_UP);
        }
        else{
            throw new BusinessException("Please provide either the volume in liters OR the total cost of the fuel purchase.");
        }
        FuelSupply fuelSupply = FuelSupplyMapper.toEntity(dto);
        fuelSupply.setTotalAmount(totalAmount);
        fuelSupply.setLiterage(literageValue);
        fuelSupply.setFuelPump(fuelPump);
        if(dto.date() != null){
            fuelSupply.setDate(dto.date());
        }else{
            fuelSupply.setDate(LocalDateTime.now());
        }
        return FuelSupplyMapper.toResponse(fuelSupplyRepository.save(fuelSupply));
    }

    @Transactional(readOnly = true)
    public List<FuelSupplyResponse> listAll() {
        return fuelSupplyRepository.findAll().stream()
                .map(FuelSupplyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FuelSupplyResponse findById(UUID id) {
        return fuelSupplyRepository.findById(id)
                .map(FuelSupplyMapper::toResponse)
                .orElseThrow(() -> new BusinessException("Fuel Supply not found."));
    }

    @Transactional
    public void delete(UUID id) {
        if (!fuelSupplyRepository.existsById(id)) {
            throw new BusinessException("Fuel Supply not found or it was deleted.");
        }
        fuelSupplyRepository.deleteById(id);
    }
}
