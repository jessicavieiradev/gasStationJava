package com.jessicavieiradev.postoGasolina.service;

import com.jessicavieiradev.postoGasolina.domain.entity.FuelPump;
import com.jessicavieiradev.postoGasolina.domain.entity.FuelType;
import com.jessicavieiradev.postoGasolina.domain.enums.Status;
import com.jessicavieiradev.postoGasolina.dto.fuelPumpDTO.FuelPumpRequest;
import com.jessicavieiradev.postoGasolina.dto.fuelPumpDTO.FuelPumpResponse;
import com.jessicavieiradev.postoGasolina.exception.BusinessException;
import com.jessicavieiradev.postoGasolina.repository.FuelPumpRepository;
import com.jessicavieiradev.postoGasolina.repository.FuelTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FuelPumpServiceTest {

    @Mock
    private FuelPumpRepository fuelPumpRepository;

    @Mock
    private FuelTypeRepository fuelTypeRepository;

    @InjectMocks
    private FuelPumpService fuelPumpService;

    private FuelType fuelType;
    private FuelPump fuelPump;
    private FuelPumpRequest request;
    private UUID pumpId;
    private UUID fuelTypeId;

    @BeforeEach
    void setUp() {
        pumpId = UUID.randomUUID();
        fuelTypeId = UUID.randomUUID();

        fuelType = new FuelType();
        fuelType.setId(fuelTypeId);
        fuelType.setName("Gasolina Comum");
        fuelType.setPrice(new BigDecimal("5.89"));
        fuelType.setStatus(Status.ativo);
        fuelType.setCreatedAt(LocalDateTime.now());

        fuelPump = new FuelPump();
        fuelPump.setId(pumpId);
        fuelPump.setName("Bomba 01");
        fuelPump.setStatus(Status.ativo);
        fuelPump.setCreatedAt(LocalDateTime.now());
        fuelPump.setFuelType(fuelType);

        request = new FuelPumpRequest("Bomba 01", fuelTypeId);
    }

    // -------------------------------------------------------------------------
    // create
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("create should save and return the fuel pump when the name does not exist and the fuel type is found")
    void create_shouldSaveAndReturnFuelPumpWhenNameDoesNotExistAndFuelTypeIsFound() {
        when(fuelPumpRepository.existsAnyByNameIgnoreCase(request.name())).thenReturn(false);
        when(fuelTypeRepository.findById(fuelTypeId)).thenReturn(Optional.of(fuelType));
        when(fuelPumpRepository.save(any(FuelPump.class))).thenReturn(fuelPump);

        FuelPumpResponse response = fuelPumpService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Bomba 01");
        assertThat(response.fuelTypeId()).isEqualTo(fuelTypeId);
        verify(fuelPumpRepository).save(any(FuelPump.class));
    }

    @Test
    @DisplayName("create should throw BusinessException when the name already exists")
    void create_shouldThrowBusinessExceptionWhenNameAlreadyExists() {
        when(fuelPumpRepository.existsAnyByNameIgnoreCase(request.name())).thenReturn(true);

        assertThatThrownBy(() -> fuelPumpService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("The fuel pump name already exists.");

        verify(fuelPumpRepository, never()).save(any());
    }

    @Test
    @DisplayName("create should throw BusinessException when fuel type is not found")
    void create_shouldThrowBusinessExceptionWhenFuelTypeIsNotFound() {
        when(fuelPumpRepository.existsAnyByNameIgnoreCase(request.name())).thenReturn(false);
        when(fuelTypeRepository.findById(fuelTypeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fuelPumpService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Fuel Type not found with ID");

        verify(fuelPumpRepository, never()).save(any());
    }

    // -------------------------------------------------------------------------
    // listAll
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("listAll should return a list with all fuel pumps")
    void listAll_shouldReturnAllFuelPumps() {
        when(fuelPumpRepository.findAll()).thenReturn(List.of(fuelPump));

        List<FuelPumpResponse> result = fuelPumpService.listAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Bomba 01");
    }

    @Test
    @DisplayName("listAll should return an empty list when there are no records")
    void listAll_shouldReturnEmptyListWhenThereAreNoRecords() {
        when(fuelPumpRepository.findAll()).thenReturn(List.of());

        assertThat(fuelPumpService.listAll()).isEmpty();
    }

    // -------------------------------------------------------------------------
    // findById
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("findById should return the fuel pump when it is found")
    void findById_shouldReturnFuelPumpWhenItIsFound() {
        when(fuelPumpRepository.findById(pumpId)).thenReturn(Optional.of(fuelPump));

        FuelPumpResponse response = fuelPumpService.findById(pumpId);

        assertThat(response.id()).isEqualTo(pumpId);
    }

    @Test
    @DisplayName("findById should throw BusinessException when fuel pump is not found")
    void findById_shouldThrowBusinessExceptionWhenFuelPumpIsNotFound() {
        when(fuelPumpRepository.findById(pumpId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fuelPumpService.findById(pumpId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Fuel Pump not found.");
    }

    // -------------------------------------------------------------------------
    // update
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("update should update the name and fuel type successfully")
    void update_shouldUpdateNameAndFuelTypeSuccessfully() {
        FuelPumpRequest updateRequest = new FuelPumpRequest("Bomba 02", fuelTypeId);

        when(fuelPumpRepository.findById(pumpId)).thenReturn(Optional.of(fuelPump));
        when(fuelTypeRepository.findById(fuelTypeId)).thenReturn(Optional.of(fuelType));
        when(fuelPumpRepository.save(any(FuelPump.class))).thenReturn(fuelPump);

        FuelPumpResponse response = fuelPumpService.update(pumpId, updateRequest);

        assertThat(response).isNotNull();
        verify(fuelPumpRepository).save(fuelPump);
    }

    @Test
    @DisplayName("update should throw BusinessException when fuel pump is not found")
    void update_shouldThrowBusinessExceptionWhenFuelPumpIsNotFound() {
        when(fuelPumpRepository.findById(pumpId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fuelPumpService.update(pumpId, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Fuel Pump not found.");
    }

    @Test
    @DisplayName("update should throw BusinessException when fuel type is not found")
    void update_shouldThrowBusinessExceptionWhenFuelTypeIsNotFound() {
        when(fuelPumpRepository.findById(pumpId)).thenReturn(Optional.of(fuelPump));
        when(fuelTypeRepository.findById(fuelTypeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fuelPumpService.update(pumpId, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Fuel Type not found.");
    }

    // -------------------------------------------------------------------------
    // delete
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("delete should call deleteById when the fuel pump exists")
    void delete_shouldDeleteWhenFuelPumpExists() {
        when(fuelPumpRepository.existsById(pumpId)).thenReturn(true);

        fuelPumpService.delete(pumpId);

        verify(fuelPumpRepository).deleteById(pumpId);
    }

    @Test
    @DisplayName("delete should throw BusinessException when fuel pump does not exist")
    void delete_shouldThrowBusinessExceptionWhenFuelPumpDoesNotExist() {
        when(fuelPumpRepository.existsById(pumpId)).thenReturn(false);

        assertThatThrownBy(() -> fuelPumpService.delete(pumpId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Fuel Pump not found.");

        verify(fuelPumpRepository, never()).deleteById(any());
    }

    // -------------------------------------------------------------------------
    // reactivate
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("reactivate should activate the fuel pump when it is inactive and the fuel type is active")
    void reactivate_shouldActivateWhenFuelPumpIsInactiveAndFuelTypeIsActive() {
        fuelPump.setStatus(Status.inativo);

        when(fuelPumpRepository.findByIdIncludingInactive(pumpId)).thenReturn(Optional.of(fuelPump));
        when(fuelPumpRepository.save(fuelPump)).thenReturn(fuelPump);

        fuelPumpService.reactivate(pumpId);

        assertThat(fuelPump.getStatus()).isEqualTo(Status.ativo);
        verify(fuelPumpRepository).save(fuelPump);
    }

    @Test
    @DisplayName("reactivate should throw BusinessException when the fuel pump is already active")
    void reactivate_shouldThrowBusinessExceptionWhenFuelPumpIsAlreadyActive() {
        // fuelPump já começa com Status.ativo no setUp
        when(fuelPumpRepository.findByIdIncludingInactive(pumpId)).thenReturn(Optional.of(fuelPump));

        assertThatThrownBy(() -> fuelPumpService.reactivate(pumpId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("This fuel pump is already active.");

        verify(fuelPumpRepository, never()).save(any());
    }

    @Test
    @DisplayName("reactivate should throw BusinessException when the associated fuel type is inactive")
    void reactivate_shouldThrowBusinessExceptionWhenAssociatedFuelTypeIsInactive() {
        fuelPump.setStatus(Status.inativo);
        fuelType.setStatus(Status.inativo); // FuelType inativo bloqueia reativação

        when(fuelPumpRepository.findByIdIncludingInactive(pumpId)).thenReturn(Optional.of(fuelPump));

        assertThatThrownBy(() -> fuelPumpService.reactivate(pumpId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("It's not possible to activate this pump: the associated fuel type is inactive");

        verify(fuelPumpRepository, never()).save(any());
    }

    @Test
    @DisplayName("reactivate should throw BusinessException when fuel pump is not found")
    void reactivate_shouldThrowBusinessExceptionWhenFuelPumpIsNotFound() {
        when(fuelPumpRepository.findByIdIncludingInactive(pumpId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fuelPumpService.reactivate(pumpId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Fuel pump not found");
    }
}