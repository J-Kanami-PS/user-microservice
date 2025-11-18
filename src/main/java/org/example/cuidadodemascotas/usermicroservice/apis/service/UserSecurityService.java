package org.example.cuidadodemascotas.usermicroservice.apis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cuidadodemascotas.usermicroservice.apis.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service("userSecurityService")
@RequiredArgsConstructor
public class UserSecurityService {

    private final UserRepository userRepository;

    /**
     * Verifica si el usuario autenticado es el dueño del recurso
     */
    public boolean isOwner(Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            log.warn("No authenticated user found");
            return false;
        }

        String currentUserEmail = auth.getName();
        log.debug("Checking if user '{}' owns resource with userId: {}", currentUserEmail, userId);

        return userRepository.findByIdAndActiveTrue(userId)
                .map(user -> {
                    boolean isOwner = user.getEmail().equals(currentUserEmail);
                    log.debug("User '{}' is owner: {}", currentUserEmail, isOwner);
                    return isOwner;
                })
                .orElse(false);
    }

    /**
     * Verifica si el usuario tiene un rol específico
     */
    public boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

    /**
     * Verifica si el usuario es ADMIN
     */
    public boolean isAdmin() {
        return hasRole("ADMIN");
    }
}