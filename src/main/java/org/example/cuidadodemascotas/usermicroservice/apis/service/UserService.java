package org.example.cuidadodemascotas.usermicroservice.apis.service;

import lombok.extern.slf4j.Slf4j;
import org.example.cuidadodemascota.commons.entities.user.User;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.UserRequestDTO;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.UserResponseDTO;
import org.example.cuidadodemascotas.usermicroservice.apis.repository.UserRepository;
import org.example.cuidadodemascotas.usermicroservice.apis.repository.RoleRepository;
import org.example.cuidadodemascotas.usermicroservice.exception.NotFoundException;
import org.example.cuidadodemascotas.usermicroservice.utils.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final UserRoleService userRoleService;

    @Value("${pagination.size.user.list:20}")
    private int defaultPageSize;

    @Value("${pagination.size.user.search:10}")
    private int searchPageSize;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       UserMapper userMapper, UserRoleService userRoleService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.userRoleService = userRoleService;
    }

    @Transactional
    public UserResponseDTO create(UserRequestDTO dto) {
        log.info("Creating user: {}", dto.getEmail());
        // Validar email único
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException(
                    "Ya existe un usuario con el email: " + dto.getEmail());
        }
        validateUserRequest(dto);
        User entity = userMapper.toEntity(dto);
        entity.setActive(true);
        User saved = userRepository.save(entity);
        log.info("User created successfully with id: {}", saved.getId());
        return userMapper.toDto(saved);
    }

    public Page<UserResponseDTO> findByFilters(String roleName, String searchTerm, int page, int size) {
        log.debug("Filtering users - roleName: {}, searchTerm: {}", roleName, searchTerm);
        int pageSize = size > 0 ? size : defaultPageSize;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<User> result = userRepository.findByRoleNameAndSearch(roleName, searchTerm, pageable);
        return result.map(userMapper::toDto);
    }

    public Page<UserResponseDTO> findByRoleId(Long roleId, String searchTerm, int page, int size) {
        log.debug("Finding users by roleId: {}", roleId);
        int pageSize = size > 0 ? size : defaultPageSize;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<User> result = userRepository.findByFilters(roleId, searchTerm, pageable);
        return result.map(userMapper::toDto);
    }

    // MÉTODOS DE CONSULTA DE ROLES
    public boolean isCarer(Long userId) {
        return userRoleService.userHasRole(userId, "CARER");
    }

    public boolean isOwner(Long userId) {
        return userRoleService.userHasRole(userId, "OWNER");
    }

    public boolean hasRole(Long userId, String roleName) {
        return userRoleService.userHasRole(userId, roleName);
    }

    public List<UserResponseDTO> findAvailableCarers() {
        log.debug("Finding available carers");
        List<User> carers = userRepository.findByRoleNameAndAvailabilityState("CARER", "AVAILABLE");
        return carers.stream()
                .map(userMapper::toDto)
                .toList();
    }

    public Page<UserResponseDTO> findByRoleName(String roleName, int page, int size) {
        log.debug("Finding users by roleName: {}", roleName);

        int pageSize = size > 0 ? size : defaultPageSize;
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<User> result = userRepository.findByRoleNameAndActiveTrue(roleName, pageable);
        return result.map(userMapper::toDto);
    }

    public UserResponseDTO findById(Long id) {
        log.debug("Finding user by id: {}", id);
        User entity = findEntityById(id);
        return userMapper.toDto(entity);
    }

    public UserResponseDTO findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        User entity = userRepository.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con email: " + email));
        return userMapper.toDto(entity);
    }

    @Transactional
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        log.info("Updating user with id: {}", id);
        User existing = findEntityById(id);
        if (dto.getEmail() != null && !dto.getEmail().equals(existing.getEmail())) {
            if (userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
                throw new IllegalArgumentException("Ya existe otro usuario con el email: " + dto.getEmail());
            }
        }
        validateUserRequest(dto);
        userMapper.updateEntityFromDto(dto, existing);
        User updated = userRepository.save(existing);
        log.info("User updated successfully: {}", id);
        return userMapper.toDto(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Soft deleting user with id: {}", id);
        User entity = findEntityById(id);
        entity.setActive(false);
        userRepository.save(entity);
        log.info("User deleted successfully: {}", id);
    }

    public long countActiveUsers() {
        return userRepository.countByActiveTrue();
    }

    public long countByRoleName(String roleName) {
        return userRepository.countByRoleName(roleName);
    }

    // ==================== MÉTODOS PRIVADOS ====================

    private User findEntityById(Long id) {
        return userRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException(id, User.class));
    }

    private void validateUserRequest(UserRequestDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }
        if (dto.getLastName() == null || dto.getLastName().isBlank()) {
            throw new IllegalArgumentException("El apellido es requerido");
        }
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new IllegalArgumentException("El email es requerido");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña es requerida");
        }
        if (dto.getPhoneNumber() == null || dto.getPhoneNumber().isBlank()) {
            throw new IllegalArgumentException("El teléfono es requerido");
        }
    }
}
