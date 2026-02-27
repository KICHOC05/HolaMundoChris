package com.bmt.HolaMundo.Models;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserSearchDTO {

    private String nombre;
    private String apellido;
    private String email;
    private Role rol;

    // 🔎 Filtro por edad
    private Integer edad;

    // 🔎 Rango de fecha de nacimiento
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimientoDesde;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimientoHasta;

    // 🔎 Rango de fecha de creación
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdAtDesde;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdAtHasta;

}