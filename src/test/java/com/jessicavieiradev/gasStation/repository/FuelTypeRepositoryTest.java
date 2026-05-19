package com.jessicavieiradev.gasStation.repository;

import com.jessicavieiradev.gasStation.domain.entity.FuelType;
import com.jessicavieiradev.gasStation.domain.enums.Status;
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
class FuelTypeRepositoryTest {

    @Autowired
    private FuelTypeRepository fuelTypeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    private FuelType savedFuelType;

    @BeforeEach
    void setUp() {
        fuelTypeRepository.deleteAll();

        FuelType fuelType = new FuelType();
        fuelType.setName("Gasolina Comum");
        fuelType.setPrice(new BigDecimal("5.89"));
        fuelType.setStatus(Status.ativo);
        fuelType.setCreatedAt(LocalDateTime.now());

        savedFuelType = fuelTypeRepository.save(fuelType);

        entityManager.flush();
    }
    // -------------------------------------------------------------------------
    // findByIdIncludingInactive (native query)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("findByIdIncludingInactive should return the record when the status is 'active'")
    void findByIdIncludingInactive_shouldReturnRecordWhenStatusIsActive() {
        Optional<FuelType> result = fuelTypeRepository.findByIdIncludingInactive(savedFuelType.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(savedFuelType.getId());
        assertThat(result.get().getName()).isEqualTo("Gasolina Comum");
    }

    @Test
    @DisplayName("findByIdIncludingInactive should return the record when the status is 'inactive'")
    void findByIdIncludingInactive_shouldReturnRecordWhenStatusIsInactive() {
        jdbcTemplate.update(
                "UPDATE fuel_types SET status = ? WHERE id = ?",
                "inativo",
                savedFuelType.getId()
        );
        entityManager.clear();
        Optional<FuelType> result = fuelTypeRepository.findByIdIncludingInactive(savedFuelType.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(savedFuelType.getId());
        assertThat(result.get().getStatus()).isEqualTo(Status.inativo);
    }

    @Test
    @DisplayName("findByIdIncludingInactive should Return Empty Optional For InexistentId")
    void findByIdIncludingInactive_shouldReturnEmptyOptionalForInexistentId() {
        Optional<FuelType> result = fuelTypeRepository.findByIdIncludingInactive(
                java.util.UUID.randomUUID()
        );

        assertThat(result).isEmpty();
    }

    // -------------------------------------------------------------------------
    // existsAnyByNameIgnoreCase
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("existsAnyByNameIgnoreCase should return true when name exist")
    void existsAnyByNameIgnoreCase_shouldReturnTrueWhenNameExists() {
        assertThat(fuelTypeRepository.existsAnyByNameIgnoreCase("Gasolina Comum")).isTrue();
    }

    @Test
    @DisplayName("existsAnyByNameIgnoreCase should return true when fuel type is inactive")
    void existsAnyByNameIgnoreCase_shouldReturnTrueWhenFuelTypeIsInactive() {

        jdbcTemplate.update(
                "UPDATE fuel_types SET status = ? WHERE id = ?",
                "inativo",
                savedFuelType.getId()
        );

        entityManager.clear();

        boolean result = fuelTypeRepository
                .existsAnyByNameIgnoreCase("Gasolina Comum");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("existsAnyByNameIgnoreCase should return false when name doesn't exist")
    void existsAnyByNameIgnoreCase_shouldReturnFalseWhenNameNotExists() {
        assertThat(fuelTypeRepository.existsAnyByNameIgnoreCase("Etanol")).isFalse();
    }

    @Test
    @DisplayName("existsAnyByNameIgnoreCaseIgnoreCase should not be case sensitive")
    void existsAnyByNameIgnoreCase_shouldNotBeCaseSensitive() {
        assertThat(fuelTypeRepository.existsAnyByNameIgnoreCase("gasolina COMUM")).isTrue();
    }
}