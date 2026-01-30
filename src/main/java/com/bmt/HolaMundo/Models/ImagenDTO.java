package com.bmt.HolaMundo.Models;

import java.time.LocalDateTime;

public class ImagenDTO {
    private Long id;
    private LocalDateTime fechaSubida;
    private String url; // URL para acceder a la imagen
    
    // Constructor2
    public ImagenDTO() {}
    
    public ImagenDTO(Long id, LocalDateTime fechaSubida, String url) {
        this.id = id;
        this.fechaSubida = fechaSubida;
        this.url = url;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getFechaSubida() {
        return fechaSubida;
    }
    
    public void setFechaSubida(LocalDateTime fechaSubida) {
        this.fechaSubida = fechaSubida;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
}