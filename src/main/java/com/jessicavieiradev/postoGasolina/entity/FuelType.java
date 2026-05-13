package com.jessicavieiradev.postoGasolina.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "fuel_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE fuel_types SET status = 'inativo' WHERE id = ?")
@SQLRestriction("status = 'ativo'")
public class FuelType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false,precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "status", nullable = false)
    private String status = "ativo";
}
