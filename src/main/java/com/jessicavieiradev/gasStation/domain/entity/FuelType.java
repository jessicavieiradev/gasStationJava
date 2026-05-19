package com.jessicavieiradev.gasStation.domain.entity;

import com.jessicavieiradev.gasStation.domain.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Size(min = 1, max = 200)
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false,precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ativo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
