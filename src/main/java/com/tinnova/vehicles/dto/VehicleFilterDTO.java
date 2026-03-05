package com.tinnova.vehicles.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record VehicleFilterDTO(
        @Size(max = 50) String marca,
        @Min(1900) Integer ano,
        @Size(max = 30) String cor,
        @Positive BigDecimal minPreco,
        @Positive BigDecimal maxPreco
) {}
