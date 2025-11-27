package com.example.Authentication.dto;

import com.example.Authentication.filter.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    private String username;
    private String firstname;
    private String lastname;
    private String country;
    private Role role;
    private Boolean enabled;
    private String password;
}