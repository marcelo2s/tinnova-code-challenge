package com.tinnova.vehicles.specification;

import com.tinnova.vehicles.entity.Vehicle;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VehicleSpecification {

    public static Specification<Vehicle> dynamicFilter(
            String marca,
            Integer ano,
            String cor,
            BigDecimal minPreco,
            BigDecimal maxPreco
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(marca != null && !marca.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("marca")),
                                "%" + marca.toLowerCase() + "%"
                        )
                );
            }

            if(ano != null) {
                predicates.add(cb.equal(root.get("ano"), ano));
            }

            if(cor != null && !cor.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("cor")),
                                "%" + cor.toLowerCase() + "%"
                        )
                );
            }

            if(minPreco != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("precoUsd"),
                                minPreco
                        )
                );
            }

            if(maxPreco != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("precoUsd"),
                                maxPreco
                        )
                );
            }

            predicates.add(cb.isTrue(root.get("ativo")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
