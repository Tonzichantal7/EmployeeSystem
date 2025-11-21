package com.example.employeemanagement.dto;

import com.example.employeemanagement.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private LocalDateTime createdAt;

}
