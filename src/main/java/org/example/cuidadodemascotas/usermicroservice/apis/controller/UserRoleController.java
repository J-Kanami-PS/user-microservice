package org.example.cuidadodemascotas.usermicroservice.apis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.UserRoleResponseDTO;
import org.example.cuidadodemascotas.usermicroservice.apis.service.UserRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user-roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    /**
     * GET /user-roles
     * Para administración - obtener todas las relaciones
     */
    @GetMapping
    public ResponseEntity<List<UserRoleResponseDTO>> getAllUserRoles() {
        log.info("GET /user-roles");
        List<UserRoleResponseDTO> userRoles = userRoleService.getAllUserRoles();
        return ResponseEntity.ok(userRoles);
    }

    /**
     * GET /user-roles/user/{userId}
     * Obtener relaciones por usuario
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserRoleResponseDTO>> getRolesByUserId(@PathVariable Long userId) {
        log.info("GET /user-roles/user/{}", userId);
        List<UserRoleResponseDTO> userRoles = userRoleService.getUserRoles(userId);
        return ResponseEntity.ok(userRoles);
    }

    /**
     * GET /user-roles/role/{roleId}
     * Obtener relaciones por rol
     */
    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<UserRoleResponseDTO>> getUsersByRoleId(@PathVariable Long roleId) {
        log.info("GET /user-roles/role/{}", roleId);
        List<UserRoleResponseDTO> userRoles = userRoleService.getUsersByRole(roleId);
        return ResponseEntity.ok(userRoles);
    }

    /**
     * DELETE /user-roles/{id}
     * Eliminar relación específica
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserRole(@PathVariable Long id) {
        log.info("DELETE /user-roles/{}", id);
        userRoleService.deleteUserRole(id);
        return ResponseEntity.noContent().build();
    }
}
