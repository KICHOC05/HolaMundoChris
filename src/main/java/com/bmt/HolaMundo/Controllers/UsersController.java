package com.bmt.HolaMundo.Controllers;

import com.bmt.HolaMundo.Models.*;
import com.bmt.HolaMundo.Service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private static final int PAGE_SIZE = 5;

    private final UserService userService;

    // =====================================================
    // 🔹 FILTER GLOBAL (evita errores de Thymeleaf)
    // =====================================================
    @ModelAttribute("filter")
    public UserSearchDTO initFilter() {
        return new UserSearchDTO();
    }

    // =====================================================
    // 🔹 CONSTRUIR PAGEABLE
    // =====================================================
    private Pageable buildPageable(int page, String sortField, String sortDir) {

        Sort sort = Sort.by(sortField);

        sort = sortDir.equalsIgnoreCase("asc")
                ? sort.ascending()
                : sort.descending();

        return PageRequest.of(page, PAGE_SIZE, sort);
    }

    // =====================================================
    // 🔹 LISTAR
    // =====================================================
    @GetMapping
    public String listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model
    ) {

        Page<UserResponseDTO> users =
                userService.getAllUsers(buildPageable(page, sortField, sortDir));

        addPaginationAttributes(model, users, page, sortField, sortDir);

        return "users/list";
    }

    // =====================================================
    // 🔹 BUSCAR (PAGINADO)
    // =====================================================
    @GetMapping("/search")
    public String searchUsers(
            UserSearchDTO filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model
    ) {

        Page<UserResponseDTO> users =
                userService.searchUsers(filter, buildPageable(page, sortField, sortDir));

        model.addAttribute("filter", filter);
        addPaginationAttributes(model, users, page, sortField, sortDir);

        return "users/list";
    }

    // =====================================================
    // 🔹 CREAR
    // =====================================================
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new UserCreateDTO());
        return "users/create";
    }

    @PostMapping("/save")
    public String saveUser(
            @Valid @ModelAttribute("user") UserCreateDTO dto,
            BindingResult result
    ) {

        if (result.hasErrors()) {
            return "users/create";
        }

        userService.createUser(dto);
        return "redirect:/users";
    }

    // =====================================================
    // 🔹 EDITAR
    // =====================================================
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {

        UserResponseDTO user = userService.getUserById(id);

        model.addAttribute("user", mapToUpdateDTO(user));
        model.addAttribute("userId", id);

        return "users/edit";
    }

    @PostMapping("/update/{id}")
    public String updateUser(
            @PathVariable Integer id,
            @Valid @ModelAttribute("user") UserUpdateDTO dto,
            BindingResult result,
            Model model
    ) {

        if (result.hasErrors()) {
            model.addAttribute("userId", id);
            return "users/edit";
        }

        userService.updateUser(id, dto);
        return "redirect:/users";
    }

    // =====================================================
    // 🔹 ELIMINAR
    // =====================================================
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    // =====================================================
    // 🔹 MAPEO PRIVADO
    // =====================================================
    private UserUpdateDTO mapToUpdateDTO(UserResponseDTO user) {

        UserUpdateDTO dto = new UserUpdateDTO();

        dto.setNombre(user.getNombre());
        dto.setApellido(user.getApellido());
        dto.setEmail(user.getEmail());
        dto.setTelefono(user.getTelefono());
        dto.setDireccion(user.getDireccion());
        dto.setFechaNacimiento(user.getFechaNacimiento());
        dto.setRol(user.getRol());

        return dto;
    }

    // =====================================================
    // 🔹 PAGINACIÓN AUXILIAR
    // =====================================================
    private void addPaginationAttributes(
            Model model,
            Page<UserResponseDTO> users,
            int page,
            String sortField,
            String sortDir
    ) {

        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("totalItems", users.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir",
                sortDir.equals("asc") ? "desc" : "asc");
    }
}