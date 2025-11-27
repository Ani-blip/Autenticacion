package com.example.Authentication.auth;

import com.example.Authentication.config.JwtService;
import com.example.Authentication.exception.UserAlreadyExistsException;
import com.example.Authentication.exception.InvalidCredentialsException;
import com.example.Authentication.filter.Role;
import com.example.Authentication.user.User;
import com.example.Authentication.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        
        // Verificar si el usuario ya existe
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("El email " + request.getUsername() + " ya está registrado");
        }
        
        // Crear usuario
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .country(request.getCountry())
                .role(request.getRole() != null ? request.getRole() : Role.USER)
                .enabled(true)
                .build();
        
        User savedUser = userRepository.save(user);
        
        // Generar token con claims adicionales
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", "ROLE_" + savedUser.getRole().name());
        extraClaims.put("userId", savedUser.getId());
        extraClaims.put("firstname", savedUser.getFirstname());
        
        String token = jwtService.generateToken(extraClaims, savedUser);
        
        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(savedUser.getId())
                .username(savedUser.getUsername())
                .firstname(savedUser.getFirstname())
                .lastname(savedUser.getLastname())
                .role(savedUser.getRole())
                .message("Usuario registrado exitosamente")
                .build();
    }
    
    @Transactional
    public AuthResponse login(LoginRequest request) {
        
        try {
            // Autenticar
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Email o contraseña incorrectos");
        }
        
        // Obtener usuario
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Usuario no encontrado"));
        
        // Generar token con claims adicionales
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", "ROLE_" + user.getRole().name());
        extraClaims.put("userId", user.getId());
        extraClaims.put("firstname", user.getFirstname());
        
        String token = jwtService.generateToken(extraClaims, user);
        
        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .role(user.getRole())
                .message("Login exitoso")
                .build();
    }
    
    public User validateToken(String token) {
        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (!jwtService.isTokenValid(token, user)) {
            throw new RuntimeException("Token inválido o expirado");
        }
        
        return user;
    }
    
    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }
}