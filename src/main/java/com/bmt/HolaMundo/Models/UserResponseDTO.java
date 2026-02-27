package com.bmt.HolaMundo.Models;

import com.bmt.HolaMundo.Models.Role;

import java.util.Date;

public class UserResponseDTO {

    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String direccion;
    private Role rol;
    private Date fechaNacimiento;
    private Date createdAt;

    public UserResponseDTO(
            int id,
            String nombre,
            String apellido,
            String email,
            String telefono,
            String direccion,
            Role rol,
            Date fechaNacimiento,
            Date createdAt
    ) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.rol = rol;
        this.fechaNacimiento = fechaNacimiento;
        this.createdAt = createdAt;
    }

	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public String getEmail() {
		return email;
	}

	public String getTelefono() {
		return telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public Role getRol() {
		return rol;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

    // Getters
    
}