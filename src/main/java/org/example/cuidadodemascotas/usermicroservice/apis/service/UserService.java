package org.example.cuidadodemascotas.usermicroservice.apis.service;

import lombok.extern.slf4j.Slf4j;
import org.example.cuidadodemascota.commons.entities.user.User;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.UserRequestDTO;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.UserResponseDTO;
import org.example.cuidadodemascotas.usermicroservice.apis.repository.UserRepository;
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
    private final UserMapper userMapper;

    @Value("${pagination.size.user.list:20}")
    private int defaultPageSize;

    @Value("${pagination.size.user.search:10}")
    private int searchPageSize;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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

        // TODO: En producción, hashear el password antes de guardar
        // entity.setPassword(passwordEncoder.encode(dto.getPassword()));

        User saved = userRepository.save(entity);
        log.info("User created successfully with id: {}", saved.getId());

        return userMapper.toDto(saved);
    }

    public UserResponseDTO findById(Long id) {
        log.debug("Finding user by id: {}", id);
        User entity = findEntityById(id);
        return userMapper.toDto(entity);
    }

    /**
     * Búsqueda con filtros opcionales
     * Acepta: roleId, searchTerm (name, lastName, email)
     */
    public Page<UserResponseDTO> findByFilters(
            Long roleId,
            String searchTerm,
            int page,
            int size) {

        log.debug("Filtering users - roleId: {}, searchTerm: {}", roleId, searchTerm);

        int pageSize = size > 0 ? size : defaultPageSize;
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<User> result = userRepository.findByFilters(roleId, searchTerm, pageable);

        return result.map(userMapper::toDto);
    }

    /**
     * Obtener usuarios por role específico
     */
    public Page<UserResponseDTO> findByRoleId(Long roleId, int page, int size) {
        log.debug("Finding users by roleId: {}", roleId);

        int pageSize = size > 0 ? size : defaultPageSize;
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<User> result = userRepository.findByRoleIdAndActiveTrue(roleId, pageable);
        return result.map(userMapper::toDto);
    }

    /**
     * Obtener usuarios por nombre de role (CARER, OWNER, etc.)
     */
    public Page<UserResponseDTO> findByRoleName(String roleName, int page, int size) {
        log.debug("Finding users by roleName: {}", roleName);

        int pageSize = size > 0 ? size : defaultPageSize;
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<User> result = userRepository.findByRoleNameAndActiveTrue(roleName, pageable);
        return result.map(userMapper::toDto);
    }

    /**
     * Buscar usuarios por nombre/apellido
     */
    public Page<UserResponseDTO> searchByName(String searchTerm, int page, int size) {
        log.debug("Searching users by name: {}", searchTerm);

        int pageSize = size > 0 ? size : searchPageSize;
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<User> result = userRepository.searchByNameOrLastName(searchTerm, pageable);
        return result.map(userMapper::toDto);
    }

    /**
     * Obtener usuario por email
     */
    public UserResponseDTO findByEmail(String email) {
        log.debug("Finding user by email: {}", email);

        User entity = userRepository.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new NotFoundException(
                        "Usuario no encontrado con email: " + email));

        return userMapper.toDto(entity);
    }

    @Transactional
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        log.info("Updating user with id: {}", id);

        User existing = findEntityById(id);

        // Validar email único (excepto el actual)
        if (dto.getEmail() != null && !dto.getEmail().equals(existing.getEmail())) {
            if (userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
                throw new IllegalArgumentException(
                        "Ya existe otro usuario con el email: " + dto.getEmail());
            }
        }

        validateUserRequest(dto);

        userMapper.updateEntityFromDto(dto, existing);

        // TODO: Si se actualiza el password, hashearlo
        // if (dto.getPassword() != null) {
        //     existing.setPassword(passwordEncoder.encode(dto.getPassword()));
        // }

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

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Obtener todos los cuidadores (role = CARER)
     */
    public List<UserResponseDTO> findAllCarers() {
        log.debug("Finding all carers");
        List<User> carers = userRepository.findByRoleNameAndActiveTrue("CARER");
        return carers.stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Obtener todos los dueños (role = OWNER)
     */
    public List<UserResponseDTO> findAllOwners() {
        log.debug("Finding all owners");
        List<User> owners = userRepository.findByRoleNameAndActiveTrue("OWNER");
        return owners.stream()
                .map(userMapper::toDto)
                .toList();
    }

    /**
     * Contar usuarios por role
     */
    public long countByRoleId(Long roleId) {
        return userRepository.countByRoleId(roleId);
    }

    public long countActiveUsers() {
        return userRepository.countByActiveTrue();
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

        if (dto.getRoleId() == null) {
            throw new IllegalArgumentException("El role es requerido");
        }
    }
}