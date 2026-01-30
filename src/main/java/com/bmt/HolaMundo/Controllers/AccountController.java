package com.bmt.HolaMundo.Controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bmt.HolaMundo.Models.AppUser;
import com.bmt.HolaMundo.Models.RegisterDto;
import com.bmt.HolaMundo.Repositories.AppUserRepository;
import com.bmt.HolaMundo.Service.RecaptchaService;

import jakarta.validation.Valid;

@Controller
public class AccountController {

    @Autowired
    private AppUserRepository repo;
    
    @Autowired
    private RecaptchaService recaptchaService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        model.addAttribute("success", false);
        model.addAttribute("siteKey", recaptchaService.getSiteKey());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            Model model,
            @Valid @ModelAttribute("registerDto") RegisterDto registerDto, // Añadido @Valid
            BindingResult result,
            @RequestParam(name = "g-recaptcha-response", required = false) String captchaResponse
    ) {
        
        // 1. Validar reCAPTCHA
        boolean isCaptchaValid = false;
        String captchaError = null;
        
        if (captchaResponse == null || captchaResponse.trim().isEmpty()) {
            captchaError = "Por favor, complete el reCAPTCHA";
        } else {
            isCaptchaValid = recaptchaService.validateRecaptcha(captchaResponse);
            if (!isCaptchaValid) {
                captchaError = "Verificación de seguridad fallida. Por favor, complete el reCAPTCHA nuevamente.";
            }
        }
        
        if (captchaError != null) {
            model.addAttribute("captchaError", captchaError);
        }
        
        // 2. Si hay errores de validación automática o reCAPTCHA, mostrar formulario
        if (result.hasErrors() || captchaError != null) {
            model.addAttribute("success", false);
            model.addAttribute("siteKey", recaptchaService.getSiteKey());
            return "register";
        }
        
        // 3. Validar si el email ya existe (validación adicional)
        AppUser existingUser = repo.findByEmail(registerDto.getEmail());
        if (existingUser != null) {
            result.addError(new FieldError("registerDto", "email", 
                "El correo ya está registrado"));
            model.addAttribute("success", false);
            model.addAttribute("siteKey", recaptchaService.getSiteKey());
            return "register";
        }

        // 4. Registrar usuario
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            AppUser newUser = new AppUser();
            newUser.setNombre(registerDto.getNombre());
            newUser.setApellido(registerDto.getApellido());
            newUser.setEmail(registerDto.getEmail());
            
            // Si teléfono está vacío, establecer como null
            if (registerDto.getTelefono() != null && !registerDto.getTelefono().trim().isEmpty()) {
                newUser.setTelefono(registerDto.getTelefono());
            } else {
                newUser.setTelefono(null);
            }
            
            newUser.setDireccion(registerDto.getDireccion());
            newUser.setFechaNacimiento(registerDto.getFechaNacimiento());
            newUser.setRol("CLIENT"); // Establecer rol obligatorio
            newUser.setContraseña(encoder.encode(registerDto.getContraseña()));

            repo.save(newUser);

            // Limpiar formulario y mostrar éxito
            model.addAttribute("registerDto", new RegisterDto());
            model.addAttribute("success", true);
            model.addAttribute("siteKey", recaptchaService.getSiteKey());

        } catch (Exception ex) {
            // Log del error para depuración
            ex.printStackTrace();
            System.err.println("Error al registrar usuario: " + ex.getMessage());
            
            result.addError(new FieldError("registerDto", "nombre", 
                "Error al registrar usuario. Por favor, intente nuevamente. Error: " + ex.getMessage()));
            model.addAttribute("success", false);
            model.addAttribute("siteKey", recaptchaService.getSiteKey());
        }

        return "register";
    }
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true) {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    if (text != null && !text.trim().isEmpty()) {
                        Date date = dateFormat.parse(text);
                        
                        // Validar que no sea fecha futura
                        if (date.after(new Date())) {
                            throw new IllegalArgumentException("La fecha de nacimiento debe ser en el pasado");
                        }
                        
                        setValue(date);
                    } else {
                        setValue(null);
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Fecha de nacimiento no válida. Use YYYY-MM-DD");
                }
            }
        }); 
    }
}