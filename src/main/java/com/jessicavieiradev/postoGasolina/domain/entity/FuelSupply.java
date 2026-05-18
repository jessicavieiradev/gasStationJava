package com.jessicavieiradev.postoGasolina.domain.entity;

import com.jessicavieiradev.postoGasolina.domain.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
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
@SQLDelete(sql = "UPDATE fuel_supplies SET status = 'inativo' WHERE id = ?")
@SQLRestriction("status = 'ativo'")
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

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ativo;

    @ManyToOne
    @JoinColumn(name = "fuel_pump_id",nullable = false)
    private FuelPump fuelPump;
}
