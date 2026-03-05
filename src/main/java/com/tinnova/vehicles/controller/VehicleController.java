package com.tinnova.vehicles.controller;

import com.tinnova.vehicles.dto.*;
import com.tinnova.vehicles.entity.Vehicle;
import com.tinnova.vehicles.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("veiculos")
@Tag(name = "Vehicles", description = "Vehicle management API")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService service;

    @GetMapping
    @Operation(summary = "List vehicles with optional filters and pagination")
    public Page<VehicleResponseDTO> list(
            @Valid VehicleFilterDTO filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sort)
        );

        return service.listByFilters(filter, pageable);
    }

    @GetMapping("/relatorios/por-marca")
    @Operation(summary = "Generate report of vehicles grouped by brand")
    public List<BrandReportDTO> report() {
        return service.reportByBrand();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Search for a vehicle by its ID")
    public VehicleResponseDTO search(@PathVariable Long id) {
        return service.searchById(id);
    }

    @PostMapping
    @Operation(summary = "Create a new vehicle record")
    public VehicleResponseDTO create(@RequestBody @Valid VehicleRequestDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing vehicle record by its ID")
    public VehicleResponseDTO update(
            @PathVariable Long id,
            @RequestBody @Valid VehicleRequestDTO dto) {

        return service.update(id, dto);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update an existing vehicle record by its ID")
    public VehicleResponseDTO patch(
            @PathVariable Long id,
            @RequestBody VehiclePatchDTO dto
            ) {
        return service.patch(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a vehicle record by its ID")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
