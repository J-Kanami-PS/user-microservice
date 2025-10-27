package org.example.cuidadodemascotas.usermicroservice.apis.service;

import lombok.extern.slf4j.Slf4j;
import org.example.cuidadodemascota.commons.entities.user.Role;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.RoleRequestDTO;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.RoleResponseDTO;
import org.example.cuidadodemascotas.usermicroservice.apis.repository.RoleRepository;
import org.example.cuidadodemascotas.usermicroservice.exception.NotFoundException;
import org.example.cuidadodemascotas.usermicroservice.utils.RoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Transactional
    public RoleResponseDTO create(RoleRequestDTO dto) {
        log.info("Creating role: {}", dto.getName());

        if (roleRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException(
                    "Ya existe un role con el nombre: " + dto.getName());
        }

        Role entity = roleMapper.toEntity(dto);
        Role saved = roleRepository.save(entity);

        log.info("Role created successfully with id: {}", saved.getId());
        return roleMapper.toDto(saved);
    }

    public RoleResponseDTO findById(Long id) {
        log.debug("Finding role by id: {}", id);
        Role entity = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, Role.class));
        return roleMapper.toDto(entity);
    }

    public RoleResponseDTO findByName(String name) {
        log.debug("Finding role by name: {}", name);
        Role entity = roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(
                        "Role no encontrado con nombre: " + name));
        return roleMapper.toDto(entity);
    }

    public List<RoleResponseDTO> findAll() {
        log.debug("Finding all roles");
        List<Role> roles = roleRepository.findAllOrdered();
        return roles.stream()
                .map(roleMapper::toDto)
                .toList();
    }

    public List<RoleResponseDTO> searchByName(String name) {
        log.debug("Searching roles by name: {}", name);
        List<Role> roles = roleRepository.searchByName(name);
        return roles.stream()
                .map(roleMapper::toDto)
                .toList();
    }

    @Transactional
    public RoleResponseDTO update(Long id, RoleRequestDTO dto) {
        log.info("Updating role with id: {}", id);

        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, Role.class));

        if (roleRepository.existsByNameAndIdNot(dto.getName(), id)) {
            throw new IllegalArgumentException(
                    "Ya existe otro role con el nombre: " + dto.getName());
        }

        roleMapper.updateEntityFromDto(dto, existing);
        Role updated = roleRepository.save(existing);

        log.info("Role updated successfully: {}", id);
        return roleMapper.toDto(updated);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting role with id: {}", id);

        Role entity = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id, Role.class));

        roleRepository.delete(entity);
        log.info("Role deleted successfully: {}", id);
    }
}