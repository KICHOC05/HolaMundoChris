package com.bmt.HolaMundo.Service;

import com.bmt.HolaMundo.Models.*;
import com.bmt.HolaMundo.Models.AppUser;
import com.bmt.HolaMundo.Repositories.AppUserRepository;
import com.bmt.HolaMundo.Specifications.UserSpecification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private AppUserRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // =========================
    // 🔹 CREATE USER
    // =========================
    public UserResponseDTO createUser(UserCreateDTO dto) {

        if (repo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        AppUser user = new AppUser();

        user.setNombre(dto.getNombre());
        user.setApellido(dto.getApellido());
        user.setEmail(dto.getEmail());
        user.setTelefono(dto.getTelefono());
        user.setDireccion(dto.getDireccion());
        user.setFechaNacimiento(dto.getFechaNacimiento());
        user.setRol(dto.getRol());

        // 🔐 Encriptar contraseña
        user.setContraseña(passwordEncoder.encode(dto.getContraseña()));

        repo.save(user);

        return mapToDTO(user);
    }

    // =========================
    // 🔹 GET ALL (paginado)
    // =========================
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return repo.findAll(pageable)
                .map(this::mapToDTO);
    }

    // =========================
    // 🔹 GET BY ID
    // =========================
    public UserResponseDTO getUserById(Integer id) {

        AppUser user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return mapToDTO(user);
    }

    // =========================
    // 🔹 UPDATE USER
    // =========================
    public UserResponseDTO updateUser(Integer id, UserUpdateDTO dto) {

        AppUser user = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setNombre(dto.getNombre());
        user.setApellido(dto.getApellido());
        user.setEmail(dto.getEmail());
        user.setTelefono(dto.getTelefono());
        user.setDireccion(dto.getDireccion());
        user.setFechaNacimiento(dto.getFechaNacimiento());
        user.setRol(dto.getRol());

        // 🔐 Solo actualizar contraseña si viene informada
        if (dto.getContraseña() != null && !dto.getContraseña().isBlank()) {
            user.setContraseña(passwordEncoder.encode(dto.getContraseña()));
        }

        repo.save(user);

        return mapToDTO(user);
    }

    // =========================
    // 🔹 DELETE USER
    // =========================
    public void deleteUser(Integer id) {

        if (!repo.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }

        repo.deleteById(id);
    }

    // =========================
    // 🔹 SEARCH USERS (dinámico)
    // =========================
    public Page<UserResponseDTO> searchUsers(
            UserSearchDTO filter,
            Pageable pageable) {

        return repo.findAll(
                UserSpecification.filterUsers(filter),
                pageable
        ).map(this::mapToDTO);
    }

    // =========================
    // 🔹 MAPPER ENTITY → DTO
    // =========================
    private UserResponseDTO mapToDTO(AppUser user) {

        return new UserResponseDTO(
                user.getId(),
                user.getNombre(),
                user.getApellido(),
                user.getEmail(),
                user.getTelefono(),
                user.getDireccion(),
                user.getRol(),
                user.getFechaNacimiento(),
                user.getCreatedAt()
        );
    }
}