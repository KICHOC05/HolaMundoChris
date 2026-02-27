package com.bmt.HolaMundo.Models;

import com.bmt.HolaMundo.Models.Role;
import jakarta.validation.constraints.*;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class UserCreateDTO {

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")
    private String nombre;

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")
    private String apellido;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "^$|^[0-9]{7,20}$")
    private String telefono;

    @Size(max = 100)
    private String direccion;

    @NotBlank
    @Size(min = 8)
    private String contraseña;

    @NotNull
    private Role rol;

    @NotNull
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaNacimiento;

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

	public Role getRol() {
		return rol;
	}

	public void setRol(Role rol) {
		this.rol = rol;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

    // Getters & Setters
    
    
    
}