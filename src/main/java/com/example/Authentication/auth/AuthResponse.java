package com.example.Authentication.auth;

import com.example.Authentication.filter.Role;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    
    private String token;
    
    @Builder.Default
    private String tokenType = "Bearer";
    
    private Integer userId;
    
    private String username;
    
    private String firstname;
    
    private String lastname;
    
    private Role role;
    
    private String message;
}