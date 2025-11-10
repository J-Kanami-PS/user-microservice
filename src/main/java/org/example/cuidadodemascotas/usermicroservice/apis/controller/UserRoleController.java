package org.example.cuidadodemascotas.usermicroservice.apis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.UserRoleResponseDTO;
import org.example.cuidadodemascotas.usermicroservice.apis.service.UserRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user-roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    /**
     * Obtener todas las relaciones usuario-rol (solo ADMIN)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserRoleResponseDTO>> getAllUserRoles() {
        log.info("GET /user-roles");
        List<UserRoleResponseDTO> userRoles = userRoleService.getAllUserRoles();
        return ResponseEntity.ok(userRoles);
    }

    /**
     * Obtener relaciones por usuario (solo ADMIN)
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserRoleResponseDTO>> getRolesByUserId(@PathVariable Long userId) {
        log.info("GET /user-roles/user/{}", userId);
        List<UserRoleResponseDTO> userRoles = userRoleService.getUserRoles(userId);
        return ResponseEntity.ok(userRoles);
    }

    /**
     * Obtener relaciones por rol (solo ADMIN)
     */
    @GetMapping("/role/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserRoleResponseDTO>> getUsersByRoleId(@PathVariable Long roleId) {
        log.info("GET /user-roles/role/{}", roleId);
        List<UserRoleResponseDTO> userRoles = userRoleService.getUsersByRole(roleId);
        return ResponseEntity.ok(userRoles);
    }

    /**
     * Eliminar relación usuario-rol (solo ADMIN)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUserRole(@PathVariable Long id) {
        log.info("DELETE /user-roles/{}", id);
        userRoleService.deleteUserRole(id);
        return ResponseEntity.noContent().build();
    }
}