package com.tinnova.vehicles.repository;

import com.tinnova.vehicles.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {
    Optional<Vehicle> findByPlaca(String placa);

    @Query("""
        SELECT v.marca, COUNT(v)
        FROM Vehicle v
        WHERE v.ativo = true
        GROUP BY v.marca
    """)
    List<Object[]> reportByBrand();
}
