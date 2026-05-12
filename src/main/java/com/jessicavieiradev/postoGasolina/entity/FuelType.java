package com.jessicavieiradev.postoGasolina.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "fuelTypes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false,precision = 10, scale = 2)
    private BigDecimal price;
}
