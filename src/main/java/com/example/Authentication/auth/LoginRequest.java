package com.example.Authentication.auth;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String username;
    
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}