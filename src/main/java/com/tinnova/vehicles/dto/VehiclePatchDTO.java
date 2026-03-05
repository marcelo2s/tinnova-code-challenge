package com.tinnova.vehicles.dto;

import java.math.BigDecimal;

public record VehiclePatchDTO(
        String marca,
        String modelo,
        Integer ano,
        String cor,
        BigDecimal preco
) {
}
