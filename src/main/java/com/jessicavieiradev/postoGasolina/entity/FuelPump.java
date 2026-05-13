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
@Table(name = "fuel_pumps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE fuel_pumps SET status = 'inativo' WHERE id = ?")
@SQLRestriction("status = 'ativo'")
public class FuelPump {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status", nullable = false)
    private String status = "ativo";

    @ManyToOne
    @JoinColumn(name = "fuel_type_id")
    private FuelType fuelType;
}
