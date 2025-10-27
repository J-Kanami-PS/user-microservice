package org.example.cuidadodemascotas.usermicroservice.utils;

import org.example.cuidadodemascota.commons.entities.user.Role;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.RoleRequestDTO;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.RoleResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleResponseDTO toDto(Role entity) {
        if (entity == null) {
            return null;
        }

        RoleResponseDTO dto = new RoleResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        return dto;
    }

    public Role toEntity(RoleRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Role entity = new Role();
        entity.setName(dto.getName());

        return entity;
    }

    public void updateEntityFromDto(RoleRequestDTO dto, Role entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
    }
}