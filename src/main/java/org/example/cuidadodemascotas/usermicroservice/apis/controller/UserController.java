package org.example.cuidadodemascotas.usermicroservice.apis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.*;
import org.example.cuidadodemascotas.usermicroservice.apis.service.UserService;
import org.example.cuidadodemascotas.usermicroservice.apis.service.UserRoleService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    /**
     * Listar usuarios
     * - ADMIN: ve todos los usuarios
     * - OWNER/CARER: solo ve su propio perfil
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER', 'CARER')")
    public ResponseEntity<Page<UserResponseDTO>> getUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("GET /users - role: {}, search: {}, page: {}, size: {}", role, search, page, size);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = auth.getName();

        // Verificar si es ADMIN
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Page<UserResponseDTO> result;

        if (isAdmin) {
            // ADMIN puede ver todos
            result = userService.findByFilters(role, search, page, size);
            log.info("Admin '{}' viewing all users", userEmail);
        } else {
            // OWNER/CARER solo ve su propio perfil
            result = userService.findByEmailFiltered(userEmail, page, size);
            log.info("User '{}' viewing own profile only", userEmail);
        }

        return ResponseEntity.ok(result);
    }

    /**
     * Cuidadores disponibles (PÚBLICO)
     */
    @GetMapping("/carers/available")
    public ResponseEntity<List<UserResponseDTO>> getAvailableCarers() {
        log.info("GET /users/carers/available");
        List<UserResponseDTO> carers = userService.findAvailableCarers();
        return ResponseEntity.ok(carers);
    }

    /**
     * Verificar si es cuidador (PÚBLICO - para validaciones de negocio)
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
     * Verificar si es dueño (PÚBLICO - para validaciones de negocio)
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
     * Verificar si tiene un rol específico (PÚBLICO - para validaciones de negocio)
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
     * Obtener roles de un usuario
     * - ADMIN: puede ver roles de cualquier usuario
     * - Usuario autenticado: solo puede ver sus propios roles
     */
    @GetMapping("/{userId}/roles")
   // @PreAuthorize("hasRole('ADMIN') or @userSecurityService.isOwner(#userId)")
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
     * Asignar rol a usuario (solo ADMIN)
     */
    @PostMapping("/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserRoleResponseDTO> assignRoleToUser(
            @PathVariable Long userId,
            @Valid @RequestBody AssignRoleRequestDTO request) {
        log.info("POST /users/{}/roles - roleId: {}", userId, request.getRoleId());
        UserRoleResponseDTO response = userRoleService.assignRoleToUser(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Remover rol de usuario (solo ADMIN)
     */
    @DeleteMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeRoleFromUser(
            @PathVariable Long userId,
            @PathVariable Long roleId) {
        log.info("DELETE /users/{}/roles/{}", userId, roleId);
        userRoleService.removeRoleFromUser(userId, roleId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtener usuario por ID
     * - ADMIN: puede ver cualquier usuario
     * - Usuario autenticado: solo puede ver su propio perfil
     */
    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN') or @userSecurityService.isOwner(#id)")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        log.info("GET /users/{}", id);
        UserResponseDTO response = userService.findById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener usuario por email
     * - ADMIN: puede ver cualquier usuario
     * - Usuario autenticado: solo puede ver su propio email
     */
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN') or authentication.name == #email")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        log.info("GET /users/email/{}", email);
        UserResponseDTO response = userService.findByEmail(email);
        return ResponseEntity.ok(response);
    }

    /**
     * Crear usuario (solo ADMIN)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto) {
        log.info("POST /users - Creating user: {}", dto.getEmail());
        UserResponseDTO created = userService.create(dto);
        log.info("User created successfully with id: {}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Actualizar usuario
     * - ADMIN: puede actualizar cualquier usuario
     * - Usuario autenticado: solo puede actualizar su propio perfil
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurityService.isOwner(#id)")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO dto) {

        log.info("PUT /users/{}", id);
        UserResponseDTO updated = userService.update(id, dto);
        log.info("User updated successfully: {}", id);
        return ResponseEntity.ok(updated);
    }

    /**
     * Eliminar usuario (solo ADMIN)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE /users/{}", id);
        userService.delete(id);
        log.info("User deleted successfully: {}", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Contar usuarios (solo ADMIN)
     */
    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countUsers() {
        log.info("GET /users/count");
        long count = userService.countActiveUsers();
        return ResponseEntity.ok(count);
    }

    /**
     * Contar usuarios por rol (solo ADMIN)
     */
    @GetMapping("/count/role/{roleName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countUsersByRole(@PathVariable String roleName) {
        log.info("GET /users/count/role/{}", roleName);
        long count = userService.countByRoleName(roleName);
        return ResponseEntity.ok(count);
    }
}