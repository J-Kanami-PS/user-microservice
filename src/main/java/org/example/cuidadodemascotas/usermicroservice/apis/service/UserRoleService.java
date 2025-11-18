package org.example.cuidadodemascotas.usermicroservice.apis.service;

import lombok.extern.slf4j.Slf4j;
import org.example.cuidadodemascota.commons.entities.user.UserRole;
import org.example.cuidadodemascota.commons.entities.user.User;
import org.example.cuidadodemascota.commons.entities.user.Role;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.UserRoleResponseDTO;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.AssignRoleRequestDTO;
import org.example.cuidadodemascotas.usermicroservice.apis.repository.UserRoleRepository;
import org.example.cuidadodemascotas.usermicroservice.apis.repository.UserRepository;
import org.example.cuidadodemascotas.usermicroservice.apis.repository.RoleRepository;
import org.example.cuidadodemascotas.usermicroservice.exception.NotFoundException;
import org.example.cuidadodemascotas.usermicroservice.utils.UserRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleMapper userRoleMapper;

    public UserRoleService(UserRoleRepository userRoleRepository,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           UserRoleMapper userRoleMapper) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleMapper = userRoleMapper;
    }

    @Transactional
    public UserRoleResponseDTO assignRoleToUser(Long userId, AssignRoleRequestDTO request) {
        log.info("Assigning role to user - userId: {}, roleId: {}", userId, request.getRoleId());
        // Validar que el usuario existe
        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new NotFoundException(userId, User.class));
        // Validar que el rol existe
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new NotFoundException(request.getRoleId(), Role.class));
        // Validar que no existe ya la relación
        if (userRoleRepository.existsByUserIdAndRoleIdAndActiveTrue(userId, request.getRoleId())) {
            throw new IllegalArgumentException(
                    "El usuario ya tiene asignado el rol: " + role.getName());
        }
        // Crear y guardar la relación
        UserRole userRole = userRoleMapper.toEntity(userId, request);
        userRole.setActive(true);
        UserRole saved = userRoleRepository.save(userRole);
        log.info("Role assigned successfully - user: {}, role: {}", userId, role.getName());
        return userRoleMapper.toDto(saved);
    }

    @Transactional
    public void removeRoleFromUser(Long userId, Long roleId) {
        log.info("Removing role from user - userId: {}, roleId: {}", userId, roleId);
        UserRole userRole = userRoleRepository.findByUserIdAndRoleIdAndActiveTrue(userId, roleId)
                .orElseThrow(() -> new NotFoundException(
                        "No se encontró la relación usuario-rol para userId: " + userId + ", roleId: " + roleId));
        // Soft delete
        userRole.setActive(false);
        userRoleRepository.save(userRole);
        log.info("Role removed successfully - user: {}, role: {}", userId, roleId);
    }

    public List<UserRoleResponseDTO> getUserRoles(Long userId) {
        log.debug("Getting roles for user: {}", userId);
        // Validar que el usuario existe
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(userId, User.class);
        }
        List<UserRole> userRoles = userRoleRepository.findByUserIdAndActiveTrue(userId);
        return userRoles.stream()
                .map(userRoleMapper::toDto)
                .toList();
    }

    public List<UserRoleResponseDTO> getUsersByRole(Long roleId) {
        log.debug("Getting users for role: {}", roleId);
        // Validar que el rol existe
        if (!roleRepository.existsById(roleId)) {
            throw new NotFoundException(roleId, Role.class);
        }
        List<UserRole> userRoles = userRoleRepository.findByRoleIdAndActiveTrue(roleId);
        return userRoles.stream()
                .map(userRoleMapper::toDto)
                .toList();
    }

    public boolean userHasRole(Long userId, String roleName) {
        log.debug("Checking if user {} has role: {}", userId, roleName);
        List<UserRole> userRoles = userRoleRepository.findByUserIdAndActiveTrue(userId);
        return userRoles.stream()
                .anyMatch(userRole ->
                        userRole.getRole() != null &&
                                roleName.equalsIgnoreCase(userRole.getRole().getName()));
    }

    public List<UserRoleResponseDTO> getAllUserRoles() {
        log.debug("Getting all user-role relationships");
        List<UserRole> userRoles = userRoleRepository.findAll().stream()
                .filter(UserRole::getActive)
                .toList();
        return userRoles.stream()
                .map(userRoleMapper::toDto)
                .toList();
    }

    @Transactional
    public void deleteUserRole(Long userRoleId) {
        log.info("Deleting user-role relationship: {}", userRoleId);
        UserRole userRole = userRoleRepository.findByIdAndActiveTrue(userRoleId)
                .orElseThrow(() -> new NotFoundException(userRoleId, UserRole.class));
        userRole.setActive(false);
        userRoleRepository.save(userRole);
        log.info("User-role relationship deleted successfully: {}", userRoleId);
    }
}
