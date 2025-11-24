package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.UserDto;
import com.example.employeemanagement.entities.Role;
import com.example.employeemanagement.entities.User;
import com.example.employeemanagement.mapper.UserMapper;
import com.example.employeemanagement.repository.UserRepository;
import com.example.employeemanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Role Management", description = "Endpoints for updating user roles and querying users")
public class UserController {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserMapper userMapper;

    @Autowired
    private final UserService userService;

    @Operation(summary = "Make user ADMIN by ID")
    @PatchMapping("/{id}/make-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> makeAdminById(@PathVariable Long id) {
        return updateRoleById(id, Role.ROLE_ADMIN);
    }

    @Operation(summary = "Make user USER by ID")
    @PatchMapping("/{id}/make-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> makeUserById(@PathVariable Long id) {
        return updateRoleById(id, Role.ROLE_USER);
    }

    private ResponseEntity<UserDto> updateRoleById(Long id, Role newRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(newRole);
        userRepository.save(user);

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @Operation(summary = "Make user ADMIN by username")
    @PatchMapping("/username/{username}/make-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> makeAdminByUsername(@PathVariable String username) {
        return updateRoleByUsername(username, Role.ROLE_ADMIN);
    }

    @Operation(summary = "Make user USER by username")
    @PatchMapping("/username/{username}/make-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> makeUserByUsername(@PathVariable String username) {
        return updateRoleByUsername(username, Role.ROLE_USER);
    }

    private ResponseEntity<UserDto> updateRoleByUsername(String username, Role newRole) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(newRole);
        userRepository.save(user);

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @Operation(summary = "Check if username exists")
    @GetMapping("/exists/username/{username}")
    public ResponseEntity<Boolean> existsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userRepository.existsByUsername(username));
    }

    @Operation(summary = "Check if email exists")
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userRepository.existsByEmail(email));
    }

    @Operation(summary = "Get user by username")
    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(userMapper.toDto(user));
    }




}
