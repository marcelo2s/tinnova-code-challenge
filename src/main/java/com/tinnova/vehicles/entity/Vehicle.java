package com.tinnova.vehicles.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "veiculos", uniqueConstraints = {
        @UniqueConstraint(columnNames = "placa")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modelo;
    private Integer ano;
    private String cor;

    @Column(nullable = false, unique = true)
    private String placa;

    @Column(nullable = false)
    private BigDecimal precoUsd;

    @Column(nullable = false)
    private Boolean ativo = true;
}
