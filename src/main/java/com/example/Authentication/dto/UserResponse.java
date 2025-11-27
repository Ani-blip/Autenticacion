package com.example.Authentication.dto;

import com.example.Authentication.filter.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Integer id;
    private String username;
    private String firstname;
    private String lastname;
    private String country;
    private Role role;
    private Boolean enabled;
}