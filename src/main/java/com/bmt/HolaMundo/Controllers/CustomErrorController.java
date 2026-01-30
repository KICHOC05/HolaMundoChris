package com.bmt.HolaMundo.Controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // Obtener el código de estado del error
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            
            // Manejar diferentes códigos de error
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404"; // Página 404 personalizada
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "error/403"; // Página 403 personalizada
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/500"; // Página 500 personalizada
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                return "error/400"; // Página 400 personalizada
            }
        }
        
        // Error genérico
        return "error/generic";
    }
}