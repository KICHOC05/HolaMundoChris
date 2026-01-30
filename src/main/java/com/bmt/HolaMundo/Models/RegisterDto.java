package com.bmt.HolaMundo.Models;

import jakarta.validation.constraints.*;
import java.util.Date;

public class RegisterDto {

    // ✅ Solo letras y espacios (incluye acentos)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    @Pattern(
        regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$",
        message = "El nombre no puede contener números ni caracteres especiales"
    )
    private String nombre;

    // ✅ Solo letras y espacios
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres")
    @Pattern(
        regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$",
        message = "El apellido no puede contener números ni caracteres especiales"
    )
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres")
    private String email;

    // 📱 Solo números (opcional - permite vacío)
    @Pattern(
        regexp = "^$|^[0-9]{7,20}$",
        message = "El teléfono solo debe contener números (7 a 20 dígitos) o estar vacío"
    )
    private String telefono;

    @Size(max = 100, message = "La dirección no puede exceder los 100 caracteres")
    private String direccion;

    // 🔐 Contraseña - VALIDACIÓN COMPLETA SOLO EN DTO
    @NotBlank(message = "Contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.#_-])[A-Za-z\\d@$!%*?&.#_-]+$",
        message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial (@$!%*?&.#_-)"
    )
    private String contraseña;

    @NotBlank(message = "Debe confirmar la contraseña")
    private String confirmarContraseña;

    // 📅 Fecha de nacimiento
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private Date fechaNacimiento;

    // ✅ Validación de coincidencia de contraseñas
    @AssertTrue(message = "Las contraseñas no coinciden")
    public boolean isPasswordMatching() {
        if (contraseña == null || confirmarContraseña == null) {
            return false;
        }
        return contraseña.equals(confirmarContraseña);
    }

    // ===== GETTERS & SETTERS =====

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getConfirmarContraseña() {
        return confirmarContraseña;
    }

    public void setConfirmarContraseña(String confirmarContraseña) {
        this.confirmarContraseña = confirmarContraseña;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}