package com.example.Authentication.admin;

import com.example.Authentication.dto.UserResponse;
import com.example.Authentication.dto.UserUpdateRequest;
import com.example.Authentication.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<List<UserResponse>> listAll() {
        List<UserResponse> list = userRepository.findAll()
            .stream()
            .map(u -> UserResponse.builder()
                    .id(u.getId())
                    .username(u.getUsername())
                    .firstname(u.getFirstname())
                    .lastname(u.getLastname())
                    .country(u.getCountry())
                    .role(u.getRole())
                    .enabled(u.getEnabled())
                    .build())
            .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id,
                                        @RequestBody UserUpdateRequest req) {
        return userRepository.findById(id)
                .map(user -> {
                    if (req.getUsername() != null) user.setUsername(req.getUsername());
                    if (req.getFirstname() != null) user.setFirstname(req.getFirstname());
                    if (req.getLastname() != null) user.setLastname(req.getLastname());
                    if (req.getCountry() != null) user.setCountry(req.getCountry());
                    if (req.getEnabled() != null) user.setEnabled(req.getEnabled());
                    if (req.getRole() != null) user.setRole(req.getRole());
                    if (req.getPassword() != null) user.setPassword(passwordEncoder.encode(req.getPassword()));
                    userRepository.save(user);
                    UserResponse resp = UserResponse.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .firstname(user.getFirstname())
                            .lastname(user.getLastname())
                            .country(user.getCountry())
                            .role(user.getRole())
                            .enabled(user.getEnabled())
                            .build();
                    return ResponseEntity.ok(resp);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        return userRepository.findById(id)
                .map(u -> {
                    userRepository.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}