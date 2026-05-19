package com.jessicavieiradev.postoGasolina.repository;

import com.jessicavieiradev.postoGasolina.domain.entity.FuelPump;
import com.jessicavieiradev.postoGasolina.domain.entity.FuelType;
import com.jessicavieiradev.postoGasolina.domain.enums.Status;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class FuelPumpRepositoryTest {

    @Autowired
    private FuelPumpRepository fuelPumpRepository;

    @Autowired
    private FuelTypeRepository fuelTypeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager  entityManager;

    private FuelPump savedPump;

    @BeforeEach
    void setUp() {
        fuelPumpRepository.deleteAll();
        fuelTypeRepository.deleteAll();

        FuelType fuelType = new FuelType();
        fuelType.setName("Diesel");
        fuelType.setPrice(new BigDecimal("6.50"));
        fuelType.setStatus(Status.ativo);
        fuelType.setCreatedAt(LocalDateTime.now());
        FuelType savedFuelType = fuelTypeRepository.save(fuelType);
        entityManager.flush();

        FuelPump pump = new FuelPump();
        pump.setName("Bomba 01");
        pump.setStatus(Status.ativo);
        pump.setCreatedAt(LocalDateTime.now());
        pump.setFuelType(savedFuelType);

        savedPump = fuelPumpRepository.save(pump);
        entityManager.flush();
    }

    // -------------------------------------------------------------------------
    // findByIdIncludingInactive
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("findByIdIncludingInactive return the pump when it is active")
    void findByIdIncludingInactive_shouldReturnPumpWhenActive() {
        Optional<FuelPump> result = fuelPumpRepository.findByIdIncludingInactive(savedPump.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(savedPump.getId());
        assertThat(result.get().getName()).isEqualTo("Bomba 01");
    }

    @Test
    @DisplayName("findByIdIncludingInactive return the pump even when it is inactive")
    void findByIdIncludingInactive_shouldReturnPumpWhenInactive() {
        jdbcTemplate.update(
                "UPDATE fuel_pumps SET status = 'inativo' WHERE id = ?",
                savedPump.getId()
        );
        entityManager.clear();

        Optional<FuelPump> result = fuelPumpRepository.findByIdIncludingInactive(savedPump.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getStatus()).isEqualTo(Status.inativo);
    }

    @Test
    @DisplayName("findByIdIncludingInactive return empty optional when id doesn't exist")
    void findByIdIncludingInactive_shouldReturnEmptyWhenIdDoesNotExist() {
        Optional<FuelPump> result = fuelPumpRepository.findByIdIncludingInactive(
                java.util.UUID.randomUUID()
        );

        assertThat(result).isEmpty();
    }

    // -------------------------------------------------------------------------
    // existsAnyByNameIgnoreCase
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("existsAnyByNameIgnoreCase return true when name exist")
    void existsAnyByNameIgnoreCase_shouldReturnTrueWhenNameDoExist() {
        assertThat(fuelPumpRepository.existsAnyByNameIgnoreCase("Bomba 01")).isTrue();
    }

    @Test
    @DisplayName("existsAnyByNameIgnoreCase should return true when fuel pump is inactive")
    void existsAnyByNameIgnoreCase_shouldReturnTrueWhenFuelPumpIsInactive() {

        jdbcTemplate.update(
                "UPDATE fuel_pumps SET status = ? WHERE id = ?",
                "inativo",
                savedPump.getId()
        );

        entityManager.clear();

        boolean result = fuelPumpRepository
                .existsAnyByNameIgnoreCase("Bomba 01");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("existsAnyByNameIgnoreCase return false when name don't exist")
    void existsAnyByNameIgnoreCase_shouldReturnFalseWhenNameDoNotExist() {
        assertThat(fuelPumpRepository.existsAnyByNameIgnoreCase("Bomba 99")).isFalse();
    }
}