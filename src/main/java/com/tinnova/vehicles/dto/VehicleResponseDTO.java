package com.tinnova.vehicles.dto;

import java.math.BigDecimal;

public record VehicleResponseDTO(
        Long id,
        String marca,
        String modelo,
        Integer ano,
        String cor,
        String placa,
        BigDecimal preco
) {}
