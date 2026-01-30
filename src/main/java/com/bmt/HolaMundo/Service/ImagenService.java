package com.bmt.HolaMundo.Service;

import com.bmt.HolaMundo.Models.ImagenDTO;
import com.bmt.HolaMundo.Models.Imagen;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ImagenService {
    ImagenDTO subirImagen(MultipartFile archivo) throws Exception;
    byte[] obtenerImagenPorId(Long id);
    List<ImagenDTO> obtenerTodasLasImagenes();
    void eliminarImagen(Long id);
}