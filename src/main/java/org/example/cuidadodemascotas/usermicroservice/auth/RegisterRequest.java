package org.example.cuidadodemascotas.usermicroservice.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<Integer> roles;
}
