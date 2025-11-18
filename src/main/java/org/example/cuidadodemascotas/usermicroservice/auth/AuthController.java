package org.example.cuidadodemascotas.usermicroservice.auth;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.cuidadodemascotas.usermicroservice.jwt.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String tokenHeader) {
        try {
            // Limpieza y extracción segura
            if (tokenHeader == null || tokenHeader.isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "message", "Encabezado Authorization vacío o ausente",
                                "timestamp", new Date().toString()
                        ));
            }

            String jwt = tokenHeader.trim();
            if (jwt.toLowerCase().startsWith("bearer ")) {
                jwt = jwt.substring(7);
            }
            jwt = jwt.replaceAll("[\\r\\n\\t ]", "");

            System.out.println("Token recibido en /auth/validate: '" + jwt + "'");
            long countDots = jwt.chars().filter(ch -> ch == '.').count();
            if (countDots != 2) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of(
                                "message", "Token malformado",
                                "details", "Un JWT válido debe contener 2 puntos (header.payload.signature)",
                                "timestamp", new Date().toString()
                        ));
            }

            // Extraer claims
            Claims claims = jwtService.getAllClaims(jwt);
            String username = claims.getSubject();
            String rolesString = claims.get("roles", String.class);
            List<String> roles = (rolesString != null)
                    ? Arrays.asList(rolesString.split(","))
                    : List.of();

            // Respuesta exitosa
            return ResponseEntity.ok(new UserInfoResponse(username, roles));

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message", "Token expirado",
                            "details", e.getMessage(),
                            "timestamp", new Date().toString()
                    ));
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(
                            "message", "Token malformado",
                            "details", e.getMessage(),
                            "timestamp", new Date().toString()
                    ));
        } catch (Exception e) {
            System.err.println("Error al validar token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", "Error en la autenticación",
                            "details", e.getMessage(),
                            "timestamp", new Date().toString()
                    ));
        }
    }
}
