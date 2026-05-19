package com.jessicavieiradev.postoGasolina.service;

import com.jessicavieiradev.postoGasolina.domain.entity.FuelType;
import com.jessicavieiradev.postoGasolina.domain.enums.Status;
import com.jessicavieiradev.postoGasolina.dto.fuelTypeDTO.FuelTypeRequest;
import com.jessicavieiradev.postoGasolina.dto.fuelTypeDTO.FuelTypeResponse;
import com.jessicavieiradev.postoGasolina.exception.BusinessException;
import com.jessicavieiradev.postoGasolina.repository.FuelTypeRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

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
class FuelTypeServiceTest {

    @Mock
    private FuelTypeRepository fuelTypeRepository;

    @InjectMocks
    private FuelTypeService fuelTypeService;

    @Autowired
    private EntityManager entityManager;

    private FuelType fuelType;
    private FuelTypeRequest request;
    private UUID fuelTypeId;

    @BeforeEach
    void setUp() {
        fuelTypeId = UUID.randomUUID();

        fuelType = new FuelType();
        fuelType.setId(fuelTypeId);
        fuelType.setName("Gasolina Aditivada");
        fuelType.setPrice(new BigDecimal("6.29"));
        fuelType.setStatus(Status.ativo);
        fuelType.setCreatedAt(LocalDateTime.now());

        request = new FuelTypeRequest("Gasolina Aditivada", new BigDecimal("6.29"));
    }

    // -------------------------------------------------------------------------
    // create
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("create should save and return the fuel type when it doesn't exist")
    void create_shouldSaveAndReturnFuelTypeWhenItDoesNotExist() {

        when(fuelTypeRepository.existsAnyByNameIgnoreCase(request.name())).thenReturn(false);
        when(fuelTypeRepository.save(any(FuelType.class))).thenReturn(fuelType);

        FuelTypeResponse response = fuelTypeService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Gasolina Aditivada");
        assertThat(response.price()).isEqualByComparingTo("6.29");
        verify(fuelTypeRepository).save(any(FuelType.class));
    }

    @Test
    @DisplayName("create should throw BusinessException when name already exists")
    void create_shouldThrowBusinessExceptionWhenNameAlreadyExists() {
        when(fuelTypeRepository.existsAnyByNameIgnoreCase(request.name())).thenReturn(true);

        assertThatThrownBy(() -> fuelTypeService.create(request))
                    .isInstanceOf(BusinessException.class)
                    .hasMessage("Fuel type already exists.");

        verify(fuelTypeRepository, never()).save(any());
    }

    // -------------------------------------------------------------------------
    // listAll
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("listAll should return a list with all fuel types")
    void listAll_shouldReturnAllFuelTypes() {
        when(fuelTypeRepository.findAll()).thenReturn(List.of(fuelType));

        List<FuelTypeResponse> result = fuelTypeService.listAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().name()).isEqualTo("Gasolina Aditivada");
    }

    @Test
    @DisplayName("listAll should return an empty list when there are no records")
    void listAll_shouldReturnEmptyListWhenThereAreNoRecords() {
        when(fuelTypeRepository.findAll()).thenReturn(List.of());

        List<FuelTypeResponse> result = fuelTypeService.listAll();

        assertThat(result).isEmpty();
    }

    // -------------------------------------------------------------------------
    // findById
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("findById should return the fuel type when it exists")
    void findById_shouldReturnFuelTypeWhenItExists() {

        when(fuelTypeRepository.findById(fuelTypeId)).thenReturn(Optional.of(fuelType));

        FuelTypeResponse response = fuelTypeService.findById(fuelTypeId);

        assertThat(response.id()).isEqualTo(fuelTypeId);
        assertThat(response.name()).isEqualTo("Gasolina Aditivada");
    }

    @Test
    @DisplayName("findById should throw BusinessException when fuel type is not found")
    void findById_shouldThrowBusinessExceptionWhenFuelTypeIsNotFound() {
        when(fuelTypeRepository.findById(fuelTypeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fuelTypeService.findById(fuelTypeId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Fuel type not found.");
    }

    // -------------------------------------------------------------------------
    // update
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("update should update the fuel type when the name is not used by another record")
    void update_shouldUpdateWhenNameIsNotUsedByAnotherRecord() {
        FuelTypeRequest updateRequest = new FuelTypeRequest("Etanol", new BigDecimal("4.50"));

        when(fuelTypeRepository.findById(fuelTypeId)).thenReturn(Optional.of(fuelType));
        when(fuelTypeRepository.existsAnyByNameIgnoreCase("Etanol")).thenReturn(false);
        when(fuelTypeRepository.save(any(FuelType.class))).thenReturn(fuelType);

        FuelTypeResponse response = fuelTypeService.update(fuelTypeId, updateRequest);

        assertThat(response).isNotNull();
        verify(fuelTypeRepository).save(fuelType);
    }

    @Test
    @DisplayName("update should allow saving with the same name from the same record")
    void update_shouldAllowSavingWithSameNameFromSameRecord() {
        // mesmo nome que já está salvo → existsByName retorna true mas o nome é igual
        when(fuelTypeRepository.findById(fuelTypeId)).thenReturn(Optional.of(fuelType));
        when(fuelTypeRepository.existsAnyByNameIgnoreCase(request.name())).thenReturn(true);
        when(fuelTypeRepository.save(any(FuelType.class))).thenReturn(fuelType);

        FuelTypeResponse response = fuelTypeService.update(fuelTypeId, request);

        assertThat(response).isNotNull();
        verify(fuelTypeRepository).save(fuelType);
    }

    @Test
    @DisplayName("update should throw BusinessException when the name is already used by another record")
    void update_shouldThrowBusinessExceptionWhenNameIsAlreadyUsedByAnotherRecord() {
        FuelTypeRequest updateRequest = new FuelTypeRequest("Diesel", new BigDecimal("7.00"));

        when(fuelTypeRepository.findById(fuelTypeId)).thenReturn(Optional.of(fuelType));
        when(fuelTypeRepository.existsAnyByNameIgnoreCase("Diesel")).thenReturn(true);

        assertThatThrownBy(() -> fuelTypeService.update(fuelTypeId, updateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Fuel type name already in use.");

        verify(fuelTypeRepository, never()).save(any());
    }

    @Test
    @DisplayName("update should throw BusinessException when fuel type is not found")
    void update_shouldThrowBusinessExceptionWhenFuelTypeIsNotFound() {
        when(fuelTypeRepository.findById(fuelTypeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fuelTypeService.update(fuelTypeId, request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Fuel type not found.");
    }

    // -------------------------------------------------------------------------
    // delete
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("delete should call deleteById when the fuel type exists")
    void delete_shouldDeleteWhenFuelTypeExists() {
        when(fuelTypeRepository.existsById(fuelTypeId)).thenReturn(true);

        fuelTypeService.delete(fuelTypeId);

        verify(fuelTypeRepository).deleteById(fuelTypeId);
    }

    @Test
    @DisplayName("delete should throw BusinessException when fuel type does not exist")
    void delete_shouldThrowBusinessExceptionWhenFuelTypeDoesNotExist() {
        when(fuelTypeRepository.existsById(fuelTypeId)).thenReturn(false);

        assertThatThrownBy(() -> fuelTypeService.delete(fuelTypeId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Fuel type not found.");

        verify(fuelTypeRepository, never()).deleteById(any());
    }

    // -------------------------------------------------------------------------
    // reactivate
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("reactivate should activate the fuel type when it is inactive")
    void reactivate_shouldActivateWhenFuelTypeIsInactive() {
        fuelType.setStatus(Status.inativo);
        when(fuelTypeRepository.findByIdIncludingInactive(fuelTypeId)).thenReturn(Optional.of(fuelType));
        when(fuelTypeRepository.save(fuelType)).thenReturn(fuelType);

        fuelTypeService.reactivate(fuelTypeId);

        assertThat(fuelType.getStatus()).isEqualTo(Status.ativo);
        verify(fuelTypeRepository).save(fuelType);
    }

    @Test
    @DisplayName("reactivate should throw BusinessException when the fuel type is already active")
    void reactivate_shouldThrowBusinessExceptionWhenFuelTypeIsAlreadyActive() {
        // fuelType já começa com Status.ativo no setUp
        when(fuelTypeRepository.findByIdIncludingInactive(fuelTypeId)).thenReturn(Optional.of(fuelType));

        assertThatThrownBy(() -> fuelTypeService.reactivate(fuelTypeId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Fuel type is already active.");

        verify(fuelTypeRepository, never()).save(any());
    }

    @Test
    @DisplayName("reactivate should throw BusinessException when fuel type is not found")
    void reactivate_shouldThrowBusinessExceptionWhenFuelTypeIsNotFound() {
        when(fuelTypeRepository.findByIdIncludingInactive(fuelTypeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fuelTypeService.reactivate(fuelTypeId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Fuel type not found.");
    }
}