package org.example.cuidadodemascotas.usermicroservice.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String token = getTokenFromRequest(request);
        final String username;

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 1. Get username from token.
            username = jwtService.getUsernameFromToken(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 2. Load user details from database.
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 3. Validate token.
                if (jwtService.isTokenValid(token, userDetails)) {
                    // 4. Get roles from token and create authorities.
                    // Note: This relies on the JwtService/Token generation adding roles as a comma-separated string.
                    String roles = (String) jwtService.getClaim(token, claims -> claims.get("roles"));

                    Collection<SimpleGrantedAuthority> authorities = Arrays.stream(roles.split(","))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    // 5. Create and set authentication in the context.
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            authorities);

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException | MalformedJwtException ex) {
            // Si el token es inválido (expirado, malformado), limpiar el contexto.
            // Esto permite que el flujo falle la autenticación/autorización más adelante
            // y que el CustomAuthenticationEntryPoint (401) responda.
            SecurityContextHolder.clearContext();
            logger.warn("JWT validation failed (Expired or Malformed): " + ex.getClass().getSimpleName());
        } catch (Exception ex) {
            // Captura cualquier otro error (ej. error de DB al cargar el usuario).
            SecurityContextHolder.clearContext();
            logger.error("Error during JWT processing: " + ex.getMessage(), ex);
        }

        // Continue the filter chain.
        filterChain.doFilter(request, response);
    }


    /**
     * Extracts the JWT token (without the 'Bearer ' prefix) from the Authorization header.
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        // Remove prefix and clean token
        String token = authHeader.substring(7).trim();
        token = token.replaceAll("[\\r\\n\\t ]", "");

        return token;
    }

}