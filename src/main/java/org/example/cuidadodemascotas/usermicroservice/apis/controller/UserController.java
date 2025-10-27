package org.example.cuidadodemascotas.usermicroservice.apis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.UserRequestDTO;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.UserResponseDTO;
import org.example.cuidadodemascotas.usermicroservice.apis.service.UserService;
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

    /**
     * GET /users?page=0&size=20
     * Obtener todos los usuarios (paginación solo en query params está OK)
     */
    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("GET /users - page={}, size={}", page, size);

        Page<UserResponseDTO> result = userService.findByFilters(null, null, page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /users/role/{roleId}?page=0&size=20
     * Obtener usuarios por role (roleId en path, paginación en query)
     */
    @GetMapping("/role/{roleId}")
    public ResponseEntity<Page<UserResponseDTO>> getUsersByRole(
            @PathVariable Long roleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("GET /users/role/{} - page={}, size={}", roleId, page, size);

        Page<UserResponseDTO> result = userService.findByRoleId(roleId, page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /users/search/{text}?page=0&size=10
     * Buscar usuarios por nombre/apellido (text en path, paginación en query)
     */
    @GetMapping("/search/{text}")
    public ResponseEntity<Page<UserResponseDTO>> searchUsers(
            @PathVariable String text,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("GET /users/search/{} - page={}, size={}", text, page, size);

        Page<UserResponseDTO> result = userService.searchByName(text, page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * GET /users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        log.info("GET /users/{}", id);
        UserResponseDTO response = userService.findById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /users/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        log.info("GET /users/email/{}", email);
        UserResponseDTO response = userService.findByEmail(email);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /users/carers
     */
    @GetMapping("/carers")
    public ResponseEntity<List<UserResponseDTO>> getCarers() {
        log.info("GET /users/carers");
        List<UserResponseDTO> carers = userService.findAllCarers();
        return ResponseEntity.ok(carers);
    }

    /**
     * GET /users/carers/paged?page=0&size=20
     */
    @GetMapping("/carers/paged")
    public ResponseEntity<Page<UserResponseDTO>> getCarersPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("GET /users/carers/paged - page={}, size={}", page, size);
        Page<UserResponseDTO> carers = userService.findByRoleName("CARER", page, size);
        return ResponseEntity.ok(carers);
    }

    /**
     * GET /users/owners
     */
    @GetMapping("/owners")
    public ResponseEntity<List<UserResponseDTO>> getOwners() {
        log.info("GET /users/owners");
        List<UserResponseDTO> owners = userService.findAllOwners();
        return ResponseEntity.ok(owners);
    }

    /**
     * GET /users/owners/paged?page=0&size=20
     */
    @GetMapping("/owners/paged")
    public ResponseEntity<Page<UserResponseDTO>> getOwnersPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("GET /users/owners/paged - page={}, size={}", page, size);
        Page<UserResponseDTO> owners = userService.findByRoleName("OWNER", page, size);
        return ResponseEntity.ok(owners);
    }

    /**
     * POST /users
     */
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto) {
        log.info("POST /users - Creating user: {}", dto.getEmail());

        UserResponseDTO created = userService.create(dto);

        log.info("User created successfully with id: {}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO dto) {

        log.info("PUT /users/{}", id);

        UserResponseDTO updated = userService.update(id, dto);

        log.info("User updated successfully: {}", id);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE /users/{} - Soft delete", id);

        userService.delete(id);

        log.info("User deleted successfully: {}", id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /users/count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUsers() {
        log.info("GET /users/count");
        long count = userService.countActiveUsers();
        return ResponseEntity.ok(count);
    }

    /**
     * GET /users/count/role/{roleId}
     */
    @GetMapping("/count/role/{roleId}")
    public ResponseEntity<Long> countUsersByRole(@PathVariable Long roleId) {
        log.info("GET /users/count/role/{}", roleId);
        long count = userService.countByRoleId(roleId);
        return ResponseEntity.ok(count);
    }
}