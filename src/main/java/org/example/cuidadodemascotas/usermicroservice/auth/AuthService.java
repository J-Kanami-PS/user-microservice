package org.example.cuidadodemascotas.usermicroservice.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cuidadodemascota.commons.entities.enums.AvailabilityStateEnum;
import org.example.cuidadodemascota.commons.entities.user.Role;
import org.example.cuidadodemascota.commons.entities.user.User;
import org.example.cuidadodemascota.commons.entities.user.UserRole;
import org.example.cuidadodemascotas.usermicroservice.apis.dto.CustomUserDetails;
import org.example.cuidadodemascotas.usermicroservice.apis.repository.RoleRepository;
import org.example.cuidadodemascotas.usermicroservice.apis.repository.UserRepository;
import org.example.cuidadodemascotas.usermicroservice.apis.repository.UserRoleRepository;
import org.example.cuidadodemascotas.usermicroservice.exception.BadRequestException;
import org.example.cuidadodemascotas.usermicroservice.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    @Autowired
    private final WebClient.Builder webClientBuilder;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final UserRoleRepository userRoleRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final AuthenticationManager authenticationManager;

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        try {
            // Spring Security hace la autenticación
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            throw new BadRequestException("Email o contraseña incorrectos.", e);
        }

        // Trae el usuario con los roles ya cargados
        User userEntity = userRepository.findByEmailWithRoles(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado o inactivo."));

        // Envolver en CustomUserDetails
        UserDetails userDetails = new CustomUserDetails(userEntity);

        // Generar el token JWT
        String token = jwtService.getToken(userDetails);

        return AuthResponse.builder()
                .token("Bearer " + token)
                .build();
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El usuario ya existe");
        }

        // DEBUG: Verificar el estado recibido
        log.info("Registering user - State received: {}", request.getState());
        log.info("State type: {}", request.getState() != null ? request.getState().getClass().getName() : "NULL");

        User user = new User();
        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());

        // ESTA ES LA LÍNEA CLAVE - establecer el estado
        if (request.getState() != null) {
            user.setState(request.getState());
        } else {
            // Valor por defecto si no se envía el estado
            user.setState(AvailabilityStateEnum.AVAILABLE);
            log.info("State was null, setting default: AVAILABLE");
        }

        log.info("User state before save: {}", user.getState());

        userRepository.save(user);

        // El resto del código para roles permanece igual
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            for (Integer roleId : request.getRoles()) {
                Role role = roleRepository.findById(Long.valueOf(roleId))
                        .orElseThrow(() -> new BadRequestException("Rol no encontrado: " + roleId));

                UserRole userRole = new UserRole();
                userRole.setUser(user);
                userRole.setRole(role);
                userRoleRepository.save(userRole);
            }
        } else {
            Role defaultRole = roleRepository.findByName("OWNER")
                    .orElseThrow(() -> new BadRequestException("Rol por defecto no encontrado"));
            UserRole defaultUserRole = new UserRole();
            defaultUserRole.setUser(user);
            defaultUserRole.setRole(defaultRole);
            userRoleRepository.save(defaultUserRole);
        }

        String token = jwtService.getTokenFromEmail(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .build();
    }
}