package com.tinnova.vehicles.service;

import com.tinnova.vehicles.dto.*;
import com.tinnova.vehicles.entity.Vehicle;
import com.tinnova.vehicles.exception.BusinessException;
import com.tinnova.vehicles.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository repository;

    @InjectMocks
    private VehicleService service;

    private Vehicle vehicle;

    @BeforeEach
    void setup() {
        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setMarca("Toyota");
        vehicle.setModelo("Corolla");
        vehicle.setAno(2020);
        vehicle.setCor("Preto");
        vehicle.setPlaca("ABC1234");
        vehicle.setPrecoUsd(new BigDecimal("10000"));
        vehicle.setAtivo(true);
    }

    @Test
    void shouldThrowExceptionWhenCreatingVehicleWithExistingPlate() {
        var dto = new VehicleRequestDTO(
                "Toyota",
                "Corolla",
                2020,
                "Preto",
                "ABC1234",
                new BigDecimal("50000")
        );

        when(repository.findByPlaca("ABC1234"))
                .thenReturn(Optional.of(vehicle));

        assertThrows(BusinessException.class, () -> service.create(dto));

        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithExistingPlate() {
        var dto = new VehicleRequestDTO(
                "Toyota",
                "Corolla",
                2020,
                "Preto",
                "XYZ9999",
                new BigDecimal("50000")
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(vehicle));

        when(repository.findByPlaca("XYZ9999"))
                .thenReturn(Optional.of(new Vehicle()));

        assertThrows(BusinessException.class, () -> service.update(1L, dto));

        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingVehicle() {
        var dto = new VehicleRequestDTO(
                "Toyota",
                "Corolla",
                2020,
                "Preto",
                "ABC1234",
                new BigDecimal("50000")
        );

        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> service.update(99L, dto));
    }

    @Test
    void shouldThrowExceptionWhenPatchingNonExistingVehicle() {
        var dto = new VehiclePatchDTO(
                "Honda",
                null,
                null,
                null,
                null
        );

        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> service.patch(99L, dto));
    }

    @Test
    void shouldCallRepositoryWithSpecificationWhenFiltering() {

        var filter = new VehicleFilterDTO(
                "Toyota",
                2020,
                "Preto",
                new BigDecimal("5000"),
                new BigDecimal("20000")
        );

        Pageable pageable = PageRequest.of(0, 10);
        Page<Vehicle> page = new PageImpl<>(List.of(vehicle));

        when(repository.findAll(
                any(org.springframework.data.jpa.domain.Specification.class),
                eq(pageable)
        )).thenReturn(page);

        Page<VehicleResponseDTO> result =
                service.listByFilters(filter, pageable);

        assertEquals(1, result.getTotalElements());
        verify(repository).findAll(any(Specification.class), eq(pageable));
    }
}