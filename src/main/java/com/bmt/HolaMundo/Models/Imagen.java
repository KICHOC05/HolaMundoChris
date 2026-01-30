package com.bmt.HolaMundo.Models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "imagenes")
public class Imagen {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Lob
    @Column(columnDefinition = "LONGBLOB", nullable = false)
    private byte[] datos;
    
    @Column(name = "fecha_subida")
    private LocalDateTime fechaSubida;
    
    // Constructor
    public Imagen() {
        this.fechaSubida = LocalDateTime.now();
    }
    
    public Imagen(byte[] datos) {
        this.datos = datos;
        this.fechaSubida = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public byte[] getDatos() {
        return datos;
    }
    
    public void setDatos(byte[] datos) {
        this.datos = datos;
    }
    
    public LocalDateTime getFechaSubida() {
        return fechaSubida;
    }
    
    public void setFechaSubida(LocalDateTime fechaSubida) {
        this.fechaSubida = fechaSubida;
    }
}