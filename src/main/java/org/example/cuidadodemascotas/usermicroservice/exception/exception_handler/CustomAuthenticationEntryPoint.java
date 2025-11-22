package org.example.cuidadodemascotas.usermicroservice.exception.exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        System.out.println("🔐 AuthenticationEntryPoint - " + method + " " + path);
        System.out.println("🔐 Exception: " + authException.getMessage());

        // ✅ PARA RUTAS PÚBLICAS, PERMITIR ACCESO
        if (isPublicPath(path)) {
            System.out.println("✅ Ruta pública - permitiendo acceso sin autenticación");
            // NO establecer código de error - dejar que la request continúe
            return;
        }

        // ✅ SOLO para rutas protegidas, enviar error de autenticación
        System.out.println("🚫 Ruta protegida sin autenticación - enviando 401");
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401, no 403
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Authentication required: " + authException.getMessage() + "\"}");
    }

    private boolean isPublicPath(String path) {
        return path.contains("/auth/") ||
                path.contains("/users/auth/") ||
                path.contains("/users/carers/available") ||
                (path.contains("/users/") && path.contains("/is-carer")) ||
                (path.contains("/users/") && path.contains("/is-owner")) ||
                (path.contains("/users/") && path.contains("/has-role/")) ||
                path.contains("/swagger-ui") ||
                path.contains("/api-docs") ||
                path.contains("/actuator");
    }
}