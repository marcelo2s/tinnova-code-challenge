package com.tinnova.vehicles.service;

import com.tinnova.vehicles.dto.*;
import com.tinnova.vehicles.entity.Vehicle;
import com.tinnova.vehicles.exception.BusinessException;
import com.tinnova.vehicles.repository.VehicleRepository;
import com.tinnova.vehicles.specification.VehicleSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleService {

    private final VehicleRepository repository;
    private final ExchangeService exchangeService;

    public VehicleResponseDTO create(VehicleRequestDTO dto) {
        repository.findByPlaca(dto.placa())
                .ifPresent(vehicle -> {
                    throw new BusinessException("Plate already exists");
                });

        Vehicle vehicle = new Vehicle();
        vehicle.setMarca(dto.marca());
        vehicle.setModelo(dto.modelo());
        vehicle.setAno(dto.ano());
        vehicle.setCor(dto.cor());
        vehicle.setPlaca(dto.placa());
        vehicle.setPrecoUsd(convertToUsd(dto.preco()));
        vehicle.setAtivo(true);

        return toResponseDTO(repository.save(vehicle));
    }

    public Page<VehicleResponseDTO> listByFilters(
            VehicleFilterDTO filter,
            Pageable pageable
    ) {

        var spec = VehicleSpecification.dynamicFilter(
                filter.marca(),
                filter.ano(),
                filter.cor(),
                filter.minPreco(),
                filter.maxPreco()
        );

        return repository.findAll(spec, pageable)
                .map(this::toResponseDTO);
    }

    public List<BrandReportDTO> reportByBrand() {
        return repository.reportByBrand()
                .stream()
                .map(obj -> new BrandReportDTO(
                        (String) obj[0],
                        ((Number) obj[1]).longValue()
                ))
                .toList();
    }

    public VehicleResponseDTO searchById(Long id) {
        Vehicle vehicle = searchEntityById(id);

        return toResponseDTO(vehicle);
    }

    public VehicleResponseDTO update(Long id, VehicleRequestDTO dto) {
        Vehicle vehicle = searchEntityById(id);

        if (!vehicle.getPlaca().equals(dto.placa())) {
            repository.findByPlaca(dto.placa())
                    .ifPresent(v -> {
                        throw new BusinessException("Plate already exists");
                    });
        }

        vehicle.setMarca(dto.marca());
        vehicle.setModelo(dto.modelo());
        vehicle.setAno(dto.ano());
        vehicle.setCor(dto.cor());
        vehicle.setPlaca(dto.placa());

        if (dto.preco() != null) {
            vehicle.setPrecoUsd(convertToUsd(dto.preco()));
        }

        return toResponseDTO(repository.save(vehicle));
    }

    public VehicleResponseDTO patch(Long id, VehiclePatchDTO dto) {
        Vehicle vehicle = searchEntityById(id);

        if (dto.marca() != null) {
            vehicle.setMarca(dto.marca());
        }
        if (dto.modelo() != null) {
            vehicle.setModelo(dto.modelo());
        }
        if (dto.ano() != null) {
            vehicle.setAno(dto.ano());
        }
        if (dto.cor() != null) {
            vehicle.setCor(dto.cor());
        }
        if (dto.preco() != null) {
            vehicle.setPrecoUsd(convertToUsd(dto.preco()));
        }

        return toResponseDTO(repository.save(vehicle));
    }

    public void delete(Long id) {
        Vehicle vehicle = searchEntityById(id);
        vehicle.setAtivo(false);
        repository.save(vehicle);
    }

    private VehicleResponseDTO toResponseDTO(Vehicle vehicle) {
        return new VehicleResponseDTO(
                vehicle.getId(),
                vehicle.getMarca(),
                vehicle.getModelo(),
                vehicle.getAno(),
                vehicle.getCor(),
                vehicle.getPlaca(),
                vehicle.getPrecoUsd()
        );
    }

    private Vehicle searchEntityById(Long id) {
        return repository.findById(id)
                .filter(Vehicle::getAtivo)
                .orElseThrow(() -> new BusinessException("Vehicle not found"));
    }

    private BigDecimal convertToUsd(BigDecimal price) {
        BigDecimal dolar = exchangeService.getDolarCurrentValue();
        return price.divide(dolar, 2, RoundingMode.HALF_UP);
    }
}
