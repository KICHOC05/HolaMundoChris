package com.bmt.HolaMundo.Controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Manejar errores de tipo (conversión de String a Date)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            RedirectAttributes redirectAttributes) {
        
        if (ex.getPropertyName() != null && ex.getPropertyName().equals("fechaNacimiento")) {
            redirectAttributes.addFlashAttribute(
                "fieldError_fechaNacimiento", 
                "Fecha de nacimiento no válida. Use el formato YYYY-MM-DD (ejemplo: 1990-01-15)"
            );
        }
        
        return "redirect:/register";
    }

    // Manejar excepciones generales
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("errorMessage", 
            "Ocurrió un error inesperado: " + ex.getMessage());
        model.addAttribute("exception", ex);
        return "error/generic";
    }
}