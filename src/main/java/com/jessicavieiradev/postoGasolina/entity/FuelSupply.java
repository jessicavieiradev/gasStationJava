package com.jessicavieiradev.postoGasolina.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
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
    private DateTimeFormat date;

    @Column(name = "total_amount",nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "literage",nullable = false)
    private BigDecimal literage;

    @ManyToOne
    @JoinColumn(name = "fuel_pump_id",nullable = false)
    private FuelPump fuelPump;
}
