package org.example.cuidadodemascotas.usermicroservice.utils;

import org.example.cuidadodemascota.commons.entities.user.User;
import org.example.cuidadodemascota.commons.entities.user.Role;
import org.example.cuidadodemascota.commons.entities.enums.AvailabilityStateEnum;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.UserRequestDTO;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.UserResponseDTO;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class UserMapper {

    public UserResponseDTO toDto(User entity) {
        if (entity == null) {
            return null;
        }

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setProfilePhoto(entity.getProfilePhoto());

        if (entity.getState() != null) {
            dto.setAvailabilityState(
                    UserResponseDTO.AvailabilityStateEnum.valueOf(entity.getState().name())
            );
        }

        dto.setCreatedAt(toOffsetDateTime(entity.getCreatedAt()));
        dto.setUpdatedAt(toOffsetDateTime(entity.getUpdatedAt()));
        dto.setActive(entity.getActive());

        return dto;
    }

    public User toEntity(UserRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        User entity = new User();
        entity.setName(dto.getName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setProfilePhoto(dto.getProfilePhoto());

        if (dto.getAvailabilityState() != null) {
            entity.setState(AvailabilityStateEnum.valueOf(dto.getAvailabilityState().name()));
        } else {
            entity.setState(AvailabilityStateEnum.AVAILABLE);
        }

        return entity;
    }

    public void updateEntityFromDto(UserRequestDTO dto, User entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }

        if (dto.getLastName() != null) {
            entity.setLastName(dto.getLastName());
        }

        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }

        // Solo actualizar password si se proporciona uno nuevo
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            entity.setPassword(dto.getPassword());
        }

        if (dto.getPhoneNumber() != null) {
            entity.setPhoneNumber(dto.getPhoneNumber());
        }

        if (dto.getProfilePhoto() != null) {
            entity.setProfilePhoto(dto.getProfilePhoto());
        }

        if (dto.getAvailabilityState() != null) {
            entity.setState(AvailabilityStateEnum.valueOf(dto.getAvailabilityState().name()));
        }
    }

    private OffsetDateTime toOffsetDateTime(java.time.LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.atOffset(ZoneOffset.UTC) : null;
    }
}