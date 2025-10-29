package org.example.cuidadodemascotas.usermicroservice.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "El email no puede estar vacío")
    String email;
    @NotBlank(message = "La contrasena no puede estar vacio")
    String password;
}