package com.example.employeemanagement.controller;

import com.example.employeemanagement.entities.Role;
import com.example.employeemanagement.entities.User;
import com.example.employeemanagement.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users", description = "User administration operations for managing roles and access")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @Operation(
            summary = "Grant admin role to a user",
            description = "Assigns the ROLE_ADMIN to a user by their ID. Only users with ADMIN privileges can perform this action. This endpoint requires JWT authentication.",
            security = { @SecurityRequirement(name = "BearerAuth") }
    )
    @Parameter(name = "id", description = "The unique ID of the user to promote to admin", required = true)
    @PatchMapping("/{id}/make-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> makeAdmin(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(Role.ROLE_ADMIN);
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }
}
