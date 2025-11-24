package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.AuthRequest;
import com.example.employeemanagement.dto.AuthResponse;
import com.example.employeemanagement.dto.RegisterRequest;
import com.example.employeemanagement.dto.UserDto;
import com.example.employeemanagement.entities.Employee;
import com.example.employeemanagement.entities.Role;
import com.example.employeemanagement.entities.User;
import com.example.employeemanagement.mapper.UserMapper;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private  final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // Register a new user using UserDto
    public UserDto register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("Username is already taken");

        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email is already in use");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER); // default role

        User savedUser = userRepository.save(user);

        return UserMapper.toDto(savedUser); // password is not returned
    }

    // Authenticate user and generate JWT
    public AuthResponse authenticate(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String jwt = jwtService.generateToken(principal, user.getRole().name());

        return new AuthResponse(jwt, "Bearer", user.getUsername(), user.getRole().name());
    }


    // Assign an employee to an admin

    // Assign an existing Employee to an existing Admin

    // Make a user an admin
    public UserDto makeAdmin(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(Role.ROLE_ADMIN); // assign admin role
        userRepository.save(user);

        return UserMapper.toDto(user);
    }

    // Get user by username
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toDto(user);
    }

    // Check if username exists
    public boolean checkUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    // Check if email exists
    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }



    // Get user along with employees managed
    public UserDto getUserWithEmployees(String username) {
        User user = userRepository.findByUsernameWithEmployee(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toDto(user);
    }

}
