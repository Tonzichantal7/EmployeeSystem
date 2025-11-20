package com.example.employeemanagement.config;

import com.example.employeemanagement.entities.Role;
import com.example.employeemanagement.entities.User;
import com.example.employeemanagement.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.username:admin}")
    private String defaultAdminUsername;

    @Value("${admin.password:Admin123@}")
    private String defaultAdminPassword;

    @Value("${admin.email:admin@example.com}")
    private String defaultAdminEmail;

    @PostConstruct
    public void init() {
        if (userRepository.findByUsername(defaultAdminUsername).isEmpty()) {
            User admin = User.builder()
                    .username(defaultAdminUsername)
                    .email(defaultAdminEmail)
                    .password(passwordEncoder.encode(defaultAdminPassword))
                    .role(Role.ROLE_ADMIN)
                    .build();

            userRepository.save(admin);
            log.info("Admin user created with username: {}", defaultAdminUsername);
        }
    }
}
