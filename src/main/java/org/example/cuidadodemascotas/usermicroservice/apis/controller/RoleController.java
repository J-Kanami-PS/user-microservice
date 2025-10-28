package org.example.cuidadodemascotas.usermicroservice.apis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.RoleRequestDTO;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.RoleResponseDTO;
import org.example.cuidadodemascotas.usermicroservice.apis.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * GET /roles
     */
    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getRoles() {
        log.info("GET /roles - Getting all roles");
        List<RoleResponseDTO> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    /**
     * GET /roles/search/{name}
     */
    @GetMapping("/search/{name}")
    public ResponseEntity<List<RoleResponseDTO>> searchRoles(@PathVariable String name) {
        log.info("GET /roles/search/{}", name);
        List<RoleResponseDTO> roles = roleService.searchByName(name);
        return ResponseEntity.ok(roles);
    }

    /**
     * GET /roles/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable Long id) {
        log.info("GET /roles/{}", id);
        RoleResponseDTO response = roleService.findById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /roles/name/{name}
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<RoleResponseDTO> getRoleByName(@PathVariable String name) {
        log.info("GET /roles/name/{}", name);
        RoleResponseDTO response = roleService.findByName(name);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /roles
     */
    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(@Valid @RequestBody RoleRequestDTO dto) {
        log.info("POST /roles - Creating role: {}", dto.getName());
        RoleResponseDTO created = roleService.create(dto);
        log.info("Role created successfully with id: {}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /roles/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleRequestDTO dto) {
        log.info("PUT /roles/{}", id);
        RoleResponseDTO updated = roleService.update(id, dto);
        log.info("Role updated successfully: {}", id);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /roles/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        log.info("DELETE /roles/{}", id);
        roleService.delete(id);
        log.info("Role deleted successfully: {}", id);
        return ResponseEntity.noContent().build();
    }
}
