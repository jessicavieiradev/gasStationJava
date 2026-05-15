package com.jessicavieiradev.postoGasolina.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "fuel_supplies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelSupply {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "date",nullable = false)
    private LocalDateTime date;

    @Column(name = "total_amount",nullable = false,precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "literage",nullable = false,precision = 10, scale = 3)
    private BigDecimal literage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "fuel_pump_id",nullable = false)
    private FuelPump fuelPump;
}
