package org.example.cuidadodemascotas.usermicroservice.auth;

import lombok.extern.slf4j.Slf4j;
import org.example.cuidadodemascota.commons.entities.user.Role;
import org.example.cuidadodemascota.commons.entities.user.User;
import org.example.cuidadodemascota.commons.entities.user.UserRole;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.CustomUserDetails;
import org.example.cuidadodemascotas.usermicroservice.apis.repository.RoleRepository;
import org.example.cuidadodemascotas.usermicroservice.apis.repository.UserRepository;
import org.example.cuidadodemascotas.usermicroservice.apis.repository.UserRoleRepository;
import org.example.cuidadodemascotas.usermicroservice.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.example.cuidadodemascotas.usermicroservice.jwt.JwtService;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    @Autowired
    private final WebClient.Builder webClientBuilder;
    @Autowired
    private UserRepository userDao;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    public AuthResponse login(LoginRequest request) {
        try {
            // Spring Security hace la autenticación
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            throw new BadRequestException("Email o contraseña incorrectos.", e);
        }

        // Traer tu entidad User
        User userEntity = userDao.findByEmailAndActiveTrue(request.getEmail())
                .orElseThrow();

        // Envolver en CustomUserDetails
        UserDetails userDetails = new CustomUserDetails(userEntity);

        // Generar el token usando UserDetails
        String token = jwtService.getToken(userDetails);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El usuario ya existe");
        }

        // Crear usuario base

        org.example.cuidadodemascota.commons.entities.user.User user =
                new org.example.cuidadodemascota.commons.entities.user.User();
        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());

        // Guardar primero el usuario para obtener su ID
        userRepository.save(user);

        // 🔹 Crear relaciones UserRole (tabla intermedia)
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            for (Integer roleId : request.getRoles()) {
                Role role = roleRepository.findById(Long.valueOf(roleId))
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleId));

                UserRole userRole = new UserRole();
                userRole.setUser(user);
                userRole.setRole(role);

                userRoleRepository.save(userRole);
            }
        } else {
            // Si no envía roles, podés asignar uno por defecto
            Role defaultRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Rol por defecto no encontrado"));
            UserRole defaultUserRole = new UserRole();
            defaultUserRole.setUser(user);
            defaultUserRole.setRole(defaultRole);
            userRoleRepository.save(defaultUserRole);
        }

        // 🔹 Generar token JWT
        String token = jwtService.getTokenFromEmail(user.getEmail());

        return new AuthResponse(token);
    }



}