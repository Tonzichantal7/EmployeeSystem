package com.example.employeemanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class EmployeeDto {

    @NotBlank
    @Schema(description = "Name of the employee.", example = "Chantal")
    private String name;

    @Schema(description = "Position of the employee in the company.", example = "Developer")
    private String position;

    @Schema(description = "Department where the employee works.", example = "IT")
    private String department;

    @Schema(description = "Date when the employee was hired.", example = "2025-11-19")
    private LocalDate hireDate;
}
