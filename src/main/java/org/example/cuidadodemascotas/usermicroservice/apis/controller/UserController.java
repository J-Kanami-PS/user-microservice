package org.example.cuidadodemascotas.usermicroservice.apis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.*;
import org.example.cuidadodemascotas.usermicroservice.apis.service.UserService;
import org.example.cuidadodemascotas.usermicroservice.apis.service.UserRoleService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRoleService userRoleService;

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getUsers(
            @RequestParam(required = false) String role,      // CARER, OWNER, ADMIN
            @RequestParam(required = false) String search,    // nombre, apellido, email
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /users - role: {}, search: {}, page: {}, size: {}", role, search, page, size);
        Page<UserResponseDTO> result = userService.findByFilters(role, search, page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * Cuidadores disponibles
     */
    @GetMapping("/carers/available")
    public ResponseEntity<List<UserResponseDTO>> getAvailableCarers() {
        log.info("GET /users/carers/available");
        List<UserResponseDTO> carers = userService.findAvailableCarers();
        return ResponseEntity.ok(carers);
    }

    /**
     * Para validaciones de negocio
     */
    @GetMapping("/{userId}/is-carer")
    public ResponseEntity<UserRoleCheckResponse> isCarer(@PathVariable Long userId) {
        log.info("GET /users/{}/is-carer", userId);
        boolean isCarer = userService.isCarer(userId);
        UserRoleCheckResponse response = new UserRoleCheckResponse();
        response.setUserId(userId);
        response.setHasRole(isCarer);
        response.setRoleName("CARER");
        return ResponseEntity.ok(response);
    }

    /**
     * Para validaciones de negocio
     */
    @GetMapping("/{userId}/is-owner")
    public ResponseEntity<UserRoleCheckResponse> isOwner(@PathVariable Long userId) {
        log.info("GET /users/{}/is-owner", userId);
        boolean isOwner = userService.isOwner(userId);
        UserRoleCheckResponse response = new UserRoleCheckResponse();
        response.setUserId(userId);
        response.setHasRole(isOwner);
        response.setRoleName("OWNER");
        return ResponseEntity.ok(response);
    }

    /**
     * Para validaciones genéricas de roles
     */
    @GetMapping("/{userId}/has-role/{roleName}")
    public ResponseEntity<UserRoleCheckResponse> hasRole(
            @PathVariable Long userId,
            @PathVariable String roleName) {
        log.info("GET /users/{}/has-role/{}", userId, roleName);
        boolean hasRole = userService.hasRole(userId, roleName);
        UserRoleCheckResponse response = new UserRoleCheckResponse();
        response.setUserId(userId);
        response.setHasRole(hasRole);
        response.setRoleName(roleName.toUpperCase());
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener todos los roles de un usuario
     */
    @GetMapping("/{userId}/roles")
    public ResponseEntity<List<RoleResponseDTO>> getUserRoles(@PathVariable Long userId) {
        log.info("GET /users/{}/roles", userId);
        List<UserRoleResponseDTO> userRoles = userRoleService.getUserRoles(userId);
        List<RoleResponseDTO> roles = userRoles.stream()
                .map(userRole -> {
                    RoleResponseDTO role = new RoleResponseDTO();
                    role.setId(userRole.getRoleId());
                    role.setName(userRole.getRoleName());
                    return role;
                })
                .toList();
        return ResponseEntity.ok(roles);
    }

    /**
     * Asignar un rol a un usuario
     */
    @PostMapping("/{userId}/roles")
    public ResponseEntity<UserRoleResponseDTO> assignRoleToUser(
            @PathVariable Long userId,
            @Valid @RequestBody AssignRoleRequestDTO request) {
        log.info("POST /users/{}/roles - roleId: {}", userId, request.getRoleId());
        UserRoleResponseDTO response = userRoleService.assignRoleToUser(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Remover un rol de un usuario
     */
    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<Void> removeRoleFromUser(
            @PathVariable Long userId,
            @PathVariable Long roleId) {
        log.info("DELETE /users/{}/roles/{}", userId, roleId);
        userRoleService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        log.info("GET /users/{}", id);
        UserResponseDTO response = userService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        log.info("GET /users/email/{}", email);
        UserResponseDTO response = userService.findByEmail(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto) {
        log.info("POST /users - Creating user: {}", dto.getEmail());
        UserResponseDTO created = userService.create(dto);
        log.info("User created successfully with id: {}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO dto) {

        log.info("PUT /users/{}", id);
        UserResponseDTO updated = userService.update(id, dto);
        log.info("User updated successfully: {}", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE /users/{}", id);
        userService.delete(id);
        log.info("User deleted successfully: {}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countUsers() {
        log.info("GET /users/count");
        long count = userService.countActiveUsers();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/role/{roleName}")
    public ResponseEntity<Long> countUsersByRole(@PathVariable String roleName) {
        log.info("GET /users/count/role/{}", roleName);
        long count = userService.countByRoleName(roleName);
        return ResponseEntity.ok(count);
    }
}
