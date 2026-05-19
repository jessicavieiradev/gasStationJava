package com.jessicavieiradev.gasStation.service;

import com.jessicavieiradev.gasStation.domain.entity.FuelPump;
import com.jessicavieiradev.gasStation.domain.entity.FuelSupply;
import com.jessicavieiradev.gasStation.domain.entity.FuelType;
import com.jessicavieiradev.gasStation.domain.enums.Status;
import com.jessicavieiradev.gasStation.dto.FuelSupplyDTO.FuelSupplyRequest;
import com.jessicavieiradev.gasStation.dto.FuelSupplyDTO.FuelSupplyResponse;
import com.jessicavieiradev.gasStation.exception.BusinessException;
import com.jessicavieiradev.gasStation.repository.FuelPumpRepository;
import com.jessicavieiradev.gasStation.repository.FuelSupplyRepository;
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
class FuelSupplyServiceTest {

    @Mock
    private FuelSupplyRepository fuelSupplyRepository;

    @Mock
    private FuelPumpRepository fuelPumpRepository;

    @InjectMocks
    private FuelSupplyService fuelSupplyService;

    private FuelType fuelType;
    private FuelPump fuelPump;
    private FuelSupply fuelSupply;
    private UUID pumpId;
    private UUID supplyId;

    private static final BigDecimal PRICE = new BigDecimal("5.00");

    @BeforeEach
    void setUp() {
        pumpId = UUID.randomUUID();
        supplyId = UUID.randomUUID();

        fuelType = new FuelType();
        fuelType.setId(UUID.randomUUID());
        fuelType.setName("Gasolina Comum");
        fuelType.setPrice(PRICE);
        fuelType.setStatus(Status.ativo);
        fuelType.setCreatedAt(LocalDateTime.now());

        fuelPump = new FuelPump();
        fuelPump.setId(pumpId);
        fuelPump.setName("Bomba 01");
        fuelPump.setStatus(Status.ativo);
        fuelPump.setCreatedAt(LocalDateTime.now());
        fuelPump.setFuelType(fuelType);

        fuelSupply = new FuelSupply();
        fuelSupply.setId(supplyId);
        fuelSupply.setDate(LocalDateTime.now());
        fuelSupply.setTotalAmount(new BigDecimal("50.000"));
        fuelSupply.setLiterage(new BigDecimal("10.000"));
        fuelSupply.setStatus(Status.ativo);
        fuelSupply.setCreatedAt(LocalDateTime.now());
        fuelSupply.setFuelPump(fuelPump);
    }

    // -------------------------------------------------------------------------
    // create
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("create should calculate totalAmount correctly when only literage is provided")
    void create_shouldCalculateTotalAmountWhenOnlyLiterageIsProvided() {
        // literage = 10 → totalAmount = 10 * 5.00 = 50.000
        FuelSupplyRequest request = new FuelSupplyRequest(null, new BigDecimal("10"), pumpId, null);

        when(fuelPumpRepository.findById(pumpId)).thenReturn(Optional.of(fuelPump));
        when(fuelSupplyRepository.save(any(FuelSupply.class))).thenReturn(fuelSupply);

        FuelSupplyResponse response = fuelSupplyService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.totalAmount()).isEqualByComparingTo("50.000");
        assertThat(response.literage()).isEqualByComparingTo("10.000");
        verify(fuelSupplyRepository).save(any(FuelSupply.class));
    }

    @Test
    @DisplayName("create should calculate literage correctly when only totalAmount is provided")
    void create_shouldCalculateLiterageWhenOnlyTotalAmountIsProvided() {
        // totalAmount = 50 → literage = 50 / 5.00 = 10.000
        FuelSupplyRequest request = new FuelSupplyRequest(new BigDecimal("50"), null, pumpId, null);

        when(fuelPumpRepository.findById(pumpId)).thenReturn(Optional.of(fuelPump));
        when(fuelSupplyRepository.save(any(FuelSupply.class))).thenReturn(fuelSupply);

        FuelSupplyResponse response = fuelSupplyService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.literage()).isEqualByComparingTo("10.000");
        verify(fuelSupplyRepository).save(any(FuelSupply.class));
    }

    @Test
    @DisplayName("create should throw BusinessException when both fields are provided")
    void create_shouldThrowBusinessExceptionWhenBothFieldsAreProvided() {
        FuelSupplyRequest request = new FuelSupplyRequest(
                new BigDecimal("50"), new BigDecimal("10"), pumpId, null
        );

        when(fuelPumpRepository.findById(pumpId)).thenReturn(Optional.of(fuelPump));

        assertThatThrownBy(() -> fuelSupplyService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Please provide either the volume in liters OR the total cost of the fuel purchase.");

        verify(fuelSupplyRepository, never()).save(any());
    }

    @Test
    @DisplayName("create should throw BusinessException when neither field is provided")
    void create_shouldThrowBusinessExceptionWhenNeitherFieldIsProvided() {
        FuelSupplyRequest request = new FuelSupplyRequest(null, null, pumpId, null);

        when(fuelPumpRepository.findById(pumpId)).thenReturn(Optional.of(fuelPump));

        assertThatThrownBy(() -> fuelSupplyService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Please provide either the volume in liters OR the total cost of the fuel purchase.");

        verify(fuelSupplyRepository, never()).save(any());
    }

    @Test
    @DisplayName("create should throw BusinessException when fuel pump is not found")
    void create_shouldThrowBusinessExceptionWhenFuelPumpIsNotFound() {
        FuelSupplyRequest request = new FuelSupplyRequest(null, new BigDecimal("10"), pumpId, null);

        when(fuelPumpRepository.findById(pumpId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fuelSupplyService.create(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Fuel Pump not found or is inactive.");

        verify(fuelSupplyRepository, never()).save(any());
    }

    @Test
    @DisplayName("create should use the date provided in the request when informed")
    void create_shouldUseRequestDateWhenProvided() {
        LocalDateTime dataEspecifica = LocalDateTime.of(2025, 1, 15, 10, 0);
        FuelSupplyRequest request = new FuelSupplyRequest(null, new BigDecimal("10"), pumpId, dataEspecifica);

        fuelSupply.setDate(dataEspecifica);
        when(fuelPumpRepository.findById(pumpId)).thenReturn(Optional.of(fuelPump));
        when(fuelSupplyRepository.save(any(FuelSupply.class))).thenReturn(fuelSupply);

        FuelSupplyResponse response = fuelSupplyService.create(request);

        assertThat(response.date()).isEqualTo(dataEspecifica);
    }

    @Test
    @DisplayName("create should use the current date when the request date is not provided")
    void create_shouldUseCurrentDateWhenRequestDateIsNotProvided() {
        FuelSupplyRequest request = new FuelSupplyRequest(null, new BigDecimal("10"), pumpId, null);

        LocalDateTime antes = LocalDateTime.now().minusSeconds(1);
        when(fuelPumpRepository.findById(pumpId)).thenReturn(Optional.of(fuelPump));
        when(fuelSupplyRepository.save(any(FuelSupply.class))).thenAnswer(inv -> {
            FuelSupply saved = inv.getArgument(0);
            // Verifica que a data foi setada como "agora" (dentro de margem de 5s)
            assertThat(saved.getDate()).isAfter(antes);
            return fuelSupply;
        });

        fuelSupplyService.create(request);

        verify(fuelSupplyRepository).save(any(FuelSupply.class));
    }

    // -------------------------------------------------------------------------
    // listAll
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("listAll should return a list with all fuel supplies")
    void listAll_shouldReturnAllFuelSupplies() {
        when(fuelSupplyRepository.findAll()).thenReturn(List.of(fuelSupply));

        List<FuelSupplyResponse> result = fuelSupplyService.listAll();

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("listAll should return an empty list when there are no fuel supplies")
    void listAll_shouldReturnEmptyListWhenThereAreNoFuelSupplies() {
        when(fuelSupplyRepository.findAll()).thenReturn(List.of());

        assertThat(fuelSupplyService.listAll()).isEmpty();
    }

    // -------------------------------------------------------------------------
    // findById
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("findById should return the fuel supply when it is found")
    void findById_shouldReturnFuelSupplyWhenItIsFound() {
        when(fuelSupplyRepository.findById(supplyId)).thenReturn(Optional.of(fuelSupply));

        FuelSupplyResponse response = fuelSupplyService.findById(supplyId);

        assertThat(response.id()).isEqualTo(supplyId);
    }

    @Test
    @DisplayName("findById should throw BusinessException when fuel supply is not found")
    void findById_shouldThrowBusinessExceptionWhenFuelSupplyIsNotFound() {
        when(fuelSupplyRepository.findById(supplyId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fuelSupplyService.findById(supplyId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Fuel Supply not found.");
    }

    // -------------------------------------------------------------------------
    // delete
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("delete should call deleteById when the fuel supply exists")
    void delete_shouldDeleteWhenFuelSupplyExists()  {
        when(fuelSupplyRepository.existsById(supplyId)).thenReturn(true);

        fuelSupplyService.delete(supplyId);

        verify(fuelSupplyRepository).deleteById(supplyId);
    }

    @Test
    @DisplayName("delete should throw BusinessException when fuel supply does not exist")
    void delete_shouldThrowBusinessExceptionWhenFuelSupplyDoesNotExist() {
        when(fuelSupplyRepository.existsById(supplyId)).thenReturn(false);

        assertThatThrownBy(() -> fuelSupplyService.delete(supplyId))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Fuel Supply not found or it was deleted.");

        verify(fuelSupplyRepository, never()).deleteById(any());
    }
}