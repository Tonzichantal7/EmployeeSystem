package com.example.employeemanagement.config;

import com.example.employeemanagement.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity // Enables method-level security annotations like @PreAuthorize
public class SecurityConfig {

    // JWT filter to intercept requests and validate tokens
    private final JwtAuthenticationFilter jwtAuthFilter;

    /**
     * Configure the security filter chain for HTTP requests.
     * This sets up which endpoints are public, which require authentication, and adds JWT validation.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF because we are using JWT (stateless authentication)
                .csrf(csrf -> csrf.disable())

                // Configure endpoint access rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints: authentication and Swagger API docs
                        .requestMatchers(
                                "/api/auth/**",         // Login, register
                                "/v3/api-docs/**",      // OpenAPI docs
                                "/swagger-ui/**"        // Swagger UI
                        ).permitAll()

                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                )

                // Stateless session: Spring Security will not store session information
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Add JWT filter before the default authentication filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Password encoder bean using BCrypt for hashing passwords.
     * Always use a secure password encoder in production.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager bean used for authenticating users in login.
     * Spring will automatically wire this with the UserDetailsService.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
