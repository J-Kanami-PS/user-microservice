package org.example.cuidadodemascotas.usermicroservice.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.cuidadodemascota.commons.entities.enums.AvailabilityStateEnum;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class RegisterRequest {
    String email;
    String name;
    String lastName;
    String password;
    String phoneNumber;
    AvailabilityStateEnum state;
    private List<Integer> roles;
}
