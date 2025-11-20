package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.AuthRequest;
import com.example.employeemanagement.dto.AuthResponse;
import com.example.employeemanagement.entities.Role;
import com.example.employeemanagement.entities.User;
import com.example.employeemanagement.exception.BadRequestException;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email is already in use");
        }

        user.setRole(Role.ROLE_USER); // default role
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public AuthResponse authenticate(AuthRequest authRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (AuthenticationException ex) {
            throw new BadRequestException("Invalid username or password");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String jwt = jwtService.generateToken(principal, user.getRole().name());

        return new AuthResponse(jwt, "Bearer", user.getUsername(), user.getRole().name());
    }
}
