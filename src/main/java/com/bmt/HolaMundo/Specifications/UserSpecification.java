package com.bmt.HolaMundo.Specifications;

import com.bmt.HolaMundo.Models.UserSearchDTO;
import com.bmt.HolaMundo.Models.AppUser;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<AppUser> filterUsers(UserSearchDTO filter) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter.getNombre() != null && !filter.getNombre().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nombre")),
                        "%" + filter.getNombre().toLowerCase() + "%"
                ));
            }

            if (filter.getApellido() != null && !filter.getApellido().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("apellido")),
                        "%" + filter.getApellido().toLowerCase() + "%"
                ));
            }

            if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                        root.get("email"),
                        filter.getEmail()
                ));
            }

            if (filter.getRol() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("rol"),
                        filter.getRol()
                ));
            }

            if (filter.getFechaNacimientoDesde() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("fechaNacimiento"),
                        filter.getFechaNacimientoDesde()
                ));
            }

            if (filter.getFechaNacimientoHasta() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("fechaNacimiento"),
                        filter.getFechaNacimientoHasta()
                ));
            }

            if (filter.getCreatedAtDesde() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        filter.getCreatedAtDesde()
                ));
            }

            if (filter.getCreatedAtHasta() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("createdAt"),
                        filter.getCreatedAtHasta()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}