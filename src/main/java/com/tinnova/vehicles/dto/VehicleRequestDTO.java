package com.tinnova.vehicles.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record VehicleRequestDTO(
        @NotBlank String marca,
        @NotBlank String modelo,
        @NotNull Integer ano,
        @NotBlank String cor,
        @NotBlank String placa,
        @NotNull BigDecimal preco
) { }
