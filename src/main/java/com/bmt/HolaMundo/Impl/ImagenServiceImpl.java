package com.bmt.HolaMundo.Impl;

import com.bmt.HolaMundo.Models.ImagenDTO;
import com.bmt.HolaMundo.Models.Imagen;
import com.bmt.HolaMundo.Repositories.ImagenRepository;
import com.bmt.HolaMundo.Service.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImagenServiceImpl implements ImagenService {
    
    @Autowired
    private ImagenRepository imagenRepository;
    
    // Tipos MIME permitidos
    private static final String JPEG_MIME = "image/jpeg";
    private static final String JPG_MIME = "image/jpg";
    
    @Override
    public ImagenDTO subirImagen(MultipartFile archivo) throws Exception {
        // Validar que el archivo no esté vacío
        if (archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }
        
        // Validar tipo de archivo
        String contentType = archivo.getContentType();
        if (!JPEG_MIME.equals(contentType) && !JPG_MIME.equals(contentType)) {
            throw new IllegalArgumentException("Solo se permiten archivos JPEG/JPG");
        }
        
        // Convertir MultipartFile a byte[]
        byte[] datos = archivo.getBytes();
        
        // Crear y guardar la entidad Imagen
        Imagen imagen = new Imagen(datos);
        Imagen imagenGuardada = imagenRepository.save(imagen);
        
        // Convertir a DTO
        return convertirADTO(imagenGuardada);
    }
    
    @Override
    public byte[] obtenerImagenPorId(Long id) {
        Imagen imagen = imagenRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Imagen no encontrada con ID: " + id));
        return imagen.getDatos();
    }
    
    @Override
    public List<ImagenDTO> obtenerTodasLasImagenes() {
        List<Imagen> imagenes = imagenRepository.findAll();
        return imagenes.stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public void eliminarImagen(Long id) {
        if (!imagenRepository.existsById(id)) {
            throw new RuntimeException("Imagen no encontrada con ID: " + id);
        }
        imagenRepository.deleteById(id);
    }
    
    private ImagenDTO convertirADTO(Imagen imagen) {
        ImagenDTO dto = new ImagenDTO();
        dto.setId(imagen.getId());
        dto.setFechaSubida(imagen.getFechaSubida());
        // URL para acceder a la imagen (asumiendo endpoint /api/imagenes/{id})
        dto.setUrl("/api/imagenes/" + imagen.getId());
        return dto;
    }
}