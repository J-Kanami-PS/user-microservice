package org.example.cuidadodemascotas.usermicroservice.auth;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.example.cuidadodemascotas.usermicroservice.jwt.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/validate")
    public ResponseEntity<UserInfoResponse> validateToken(@RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        Claims claims = jwtService.getAllClaims(jwt);
        String username = claims.getSubject();

        String rolesString = claims.get("roles", String.class);
        List<String> roles = Arrays.asList(rolesString.split(","));

        return ResponseEntity.ok(new UserInfoResponse(username, roles));
    }

}