package com.bmt.HolaMundo.Controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.bmt.HolaMundo.Models.AppUser;
import com.bmt.HolaMundo.Models.RegisterDto;
import com.bmt.HolaMundo.Models.Role;
import com.bmt.HolaMundo.Repositories.AppUserRepository;
import com.bmt.HolaMundo.Service.RecaptchaService;

import jakarta.validation.Valid;

@Controller
public class AccountController {

    @Autowired
    private AppUserRepository repo;

    @Autowired
    private RecaptchaService recaptchaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ========================
    // LOGIN
    // ========================
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // ========================
    // FORM REGISTER
    // ========================
    @GetMapping("/register")
    public String register(Model model) {

        model.addAttribute("registerDto", new RegisterDto());
        model.addAttribute("siteKey", recaptchaService.getSiteKey());
        return "register";
    }

    // ========================
    // REGISTER USER
    // ========================
    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("registerDto") RegisterDto dto,
            BindingResult result,
            @RequestParam(name = "g-recaptcha-response", required = false) String captchaResponse,
            Model model
    ) {

        // 🔐 1. Validar reCAPTCHA
        if (!validateCaptcha(captchaResponse)) {
            model.addAttribute("captchaError",
                    "Verificación de seguridad fallida. Complete el reCAPTCHA.");
            return reloadRegister(model);
        }

        // 📝 2. Validaciones automáticas
        if (result.hasErrors()) {
            return reloadRegister(model);
        }

        // 📧 3. Validar email duplicado
        if (repo.existsByEmail(dto.getEmail())) {
            result.addError(new FieldError("registerDto", "email",
                    "El correo ya está registrado"));
            return reloadRegister(model);
        }

        try {
            // 👤 Crear usuario
            AppUser user = new AppUser();
            user.setNombre(dto.getNombre());
            user.setApellido(dto.getApellido());
            user.setEmail(dto.getEmail());
            user.setTelefono(
                    dto.getTelefono() == null || dto.getTelefono().isBlank()
                            ? null
                            : dto.getTelefono()
            );
            user.setDireccion(dto.getDireccion());
            user.setFechaNacimiento(dto.getFechaNacimiento());

            // 🔥 ENUM en vez de String
            user.setRol(Role.ROLE_USER);

            // 🔐 Encriptar contraseña
            user.setContraseña(passwordEncoder.encode(dto.getContraseña()));

            repo.save(user);

            model.addAttribute("success", true);
            model.addAttribute("registerDto", new RegisterDto());

        } catch (DataIntegrityViolationException ex) {

            result.addError(new FieldError("registerDto", "email",
                    "El correo electrónico ya está registrado"));
            return reloadRegister(model);

        } catch (Exception ex) {

            result.addError(new FieldError("registerDto", "nombre",
                    "Error al registrar usuario. Intente nuevamente."));
            return reloadRegister(model);
        }

        return reloadRegister(model);
    }

    // ========================
    // MÉTODOS PRIVADOS
    // ========================

    private boolean validateCaptcha(String captchaResponse) {
        return captchaResponse != null
                && !captchaResponse.trim().isEmpty()
                && recaptchaService.validateRecaptcha(captchaResponse);
    }

    private String reloadRegister(Model model) {
        model.addAttribute("siteKey", recaptchaService.getSiteKey());
        return "register";
    }

    // ========================
    // BINDER FECHA
    // ========================
    @InitBinder
    public void initBinder(WebDataBinder binder) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(dateFormat, true) {
                    @Override
                    public void setAsText(String text) {

                        try {
                            if (text != null && !text.trim().isEmpty()) {

                                Date date = dateFormat.parse(text);

                                if (date.after(new Date())) {
                                    throw new IllegalArgumentException(
                                            "La fecha debe ser en el pasado");
                                }

                                setValue(date);
                            } else {
                                setValue(null);
                            }

                        } catch (Exception e) {
                            throw new IllegalArgumentException(
                                    "Fecha inválida. Use formato YYYY-MM-DD");
                        }
                    }
                 // ========================
                 // LOGIN
                 // ========================
                 @GetMapping("/login")
                 public String login(
                         @RequestParam(value = "error", required = false) String error,
                         @RequestParam(value = "logout", required = false) String logout,
                         Model model
                 ) {

                     if (error != null) {
                         model.addAttribute("error", "Correo o contraseña incorrectos");
                     }

                     if (logout != null) {
                         model.addAttribute("message", "Sesión cerrada correctamente");
                     }

                     return "login";
                 }
                });
    }
}