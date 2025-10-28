package org.example.cuidadodemascotas.usermicroservice.utils;

import org.example.cuidadodemascota.commons.entities.user.UserRole;
import org.example.cuidadodemascota.commons.entities.user.User;
import org.example.cuidadodemascota.commons.entities.user.Role;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.UserRoleRequestDTO;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.UserRoleResponseDTO;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class UserRoleMapper {

    public UserRoleResponseDTO toDto(UserRole entity) {
        if (entity == null) {
            return null;
        }

        UserRoleResponseDTO dto = new UserRoleResponseDTO();
        //dto.setUserRoleId(entity.getId());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        dto.setRoleId(entity.getRole() != null ? entity.getRole().getId() : null);
        dto.setCreatedAt(toOffsetDateTime(entity.getCreatedAt()));
        dto.setUpdatedAt(toOffsetDateTime(entity.getUpdatedAt()));
        dto.setActive(entity.getActive());

        return dto;
    }

    public UserRole toEntity(UserRoleRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        UserRole entity = new UserRole();

        if (dto.getUserId() != null) {
            User user = new User();
            user.setId(dto.getUserId());
            entity.setUser(user);
        }

        if (dto.getRoleId() != null) {
            Role role = new Role();
            role.setId(dto.getRoleId());
            entity.setRole(role);
        }

        return entity;
    }

    private OffsetDateTime toOffsetDateTime(java.time.LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.atOffset(ZoneOffset.UTC) : null;
    }
}