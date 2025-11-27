package com.example.Authentication.auth;

import com.example.Authentication.filter.Role;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    private String username;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String firstname;
    
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    private String lastname;
    
    @Size(max = 50, message = "El país no puede exceder 50 caracteres")
    private String country;
    
    // Por defecto se registra como USER (cliente)
    private Role role = Role.USER;
}