package com.bmt.HolaMundo.Controllers;

import com.bmt.HolaMundo.Models.ImagenDTO;
import com.bmt.HolaMundo.Service.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/imagenes")
@CrossOrigin(origins = "*")
public class ImagenController {
    
    @Autowired
    private ImagenService imagenService;
    
    @PostMapping("/subir")
    public ResponseEntity<?> subirImagen(@RequestParam("imagen") MultipartFile archivo) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            ImagenDTO imagenDTO = imagenService.subirImagen(archivo);
            response.put("success", true);
            response.put("message", "Imagen subida exitosamente");
            response.put("data", imagenDTO);
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al subir la imagen: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable Long id) {
        try {
            byte[] imagenData = imagenService.obtenerImagenPorId(id);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Siempre será JPEG/JPG
            headers.setContentLength(imagenData.length);
            
            return new ResponseEntity<>(imagenData, headers, HttpStatus.OK);
            
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<?> obtenerTodasLasImagenes() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<ImagenDTO> imagenes = imagenService.obtenerTodasLasImagenes();
            response.put("success", true);
            response.put("data", imagenes);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al obtener las imágenes");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarImagen(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            imagenService.eliminarImagen(id);
            response.put("success", true);
            response.put("message", "Imagen eliminada exitosamente");
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}