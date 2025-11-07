package org.example.cuidadodemascotas.usermicroservice.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
// NO USAR Decoders.BASE64
import java.nio.charset.StandardCharsets; // <-- ¡ASEGÚRATE DE AÑADIR ESTE IMPORT!
import io.jsonwebtoken.security.Keys;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    @Value("${app.jwt.expiration.duration}")
    private int EXPIRATION_DURATION;

    // Generar token con los roles prefijados con "ROLE_"
    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user);
    }

    // Metodo privado para generar el token JWT
    private String getToken(Map<String, Object> extraClaims, UserDetails user) {
        Map<String, Object> claims = new HashMap<>(extraClaims);

        // Convertir las autoridades (roles) y prefijarlas con "ROLE_"
        String roles = user.getAuthorities().stream()
                // ¡CORRECCIÓN #1: Añadir .toUpperCase()!
                .map(authority -> "ROLE_" + authority.getAuthority().toUpperCase())
                .collect(Collectors.joining(","));

        // Añadir los roles al payload del JWT
        claims.put("roles", roles);

        // Crear el token JWT
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DURATION))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Obtener la clave para firmar el token
    private Key getKey() {
        // ¡CORRECCIÓN #2: Usar UTF_8 en lugar de Base64!
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Obtener el nombre de usuario desde el token JWT
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    // Verificar si el token es válido
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Obtener todas las claims del token
    public Claims getAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Obtener un claim específico desde el token
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Obtener la fecha de expiración del token
    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    // Verificar si el token ha expirado
    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    public String getTokenFromEmail(String email) {
        User user = new org.springframework.security.core.userdetails.User(
                email,
                "",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        return getToken(user);
    }
}