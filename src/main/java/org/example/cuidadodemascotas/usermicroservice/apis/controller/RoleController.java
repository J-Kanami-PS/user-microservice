package org.example.cuidadodemascotas.usermicroservice.apis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.RoleRequestDTO;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.RoleResponseDTO;
import org.example.cuidadodemascotas.usermicroservice.apis.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * Obtener todos los roles (solo ADMIN)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleResponseDTO>> getRoles() {
        log.info("GET /roles - Getting all roles");
        List<RoleResponseDTO> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    /**
     * Buscar roles por nombre (solo ADMIN)
     */
    @GetMapping("/search/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleResponseDTO>> searchRoles(@PathVariable String name) {
        log.info("GET /roles/search/{}", name);
        List<RoleResponseDTO> roles = roleService.searchByName(name);
        return ResponseEntity.ok(roles);
    }

    /**
     * Obtener rol por ID (solo ADMIN)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable Long id) {
        log.info("GET /roles/{}", id);
        RoleResponseDTO response = roleService.findById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener rol por nombre (solo ADMIN)
     */
    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleResponseDTO> getRoleByName(@PathVariable String name) {
        log.info("GET /roles/name/{}", name);
        RoleResponseDTO response = roleService.findByName(name);
        return ResponseEntity.ok(response);
    }

    /**
     * Crear rol (solo ADMIN)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleResponseDTO> createRole(@Valid @RequestBody RoleRequestDTO dto) {
        log.info("POST /roles - Creating role: {}", dto.getName());
        RoleResponseDTO created = roleService.create(dto);
        log.info("Role created successfully with id: {}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Actualizar rol (solo ADMIN)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleResponseDTO> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleRequestDTO dto) {
        log.info("PUT /roles/{}", id);
        RoleResponseDTO updated = roleService.update(id, dto);
        log.info("Role updated successfully: {}", id);
        return ResponseEntity.ok(updated);
    }

    /**
     * Eliminar rol (solo ADMIN)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        log.info("DELETE /roles/{}", id);
        roleService.delete(id);
        log.info("Role deleted successfully: {}", id);
        return ResponseEntity.noContent().build();
    }
}