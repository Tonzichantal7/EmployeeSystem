package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeDto;
import com.example.employeemanagement.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Employees", description = "Employee CRUD operations")
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;


    @Operation(summary = "Create a new employee", description = "Only ADMIN can create an employee")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EmployeeDto> create(@Valid @RequestBody EmployeeDto employeeDto) {
        return ResponseEntity.ok(employeeService.create(employeeDto));
    }

    @Operation(summary = "Get all employees", description = "USER and ADMIN can view all employees")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> all() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @Operation(summary = "Get an employee by ID", description = "USER and ADMIN can view a single employee")
    @Parameter(name = "id", description = "ID of the employee to retrieve", required = true)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> one(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @Operation(summary = "Promote an employee to admin and update admin_employee table")
    @PatchMapping("/{employeeId}/promote-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> promoteEmployeeToAdmin(@PathVariable Long employeeId) {
        employeeService.promoteEmployeeToAdmin(employeeId);
        return ResponseEntity.ok("Employee successfully promoted to admin");
    }

    @Operation(summary = "Update an employee", description = "Only ADMIN can update employee details")
    @Parameter(name = "id", description = "ID of the employee to update", required = true)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> update(@PathVariable Long id, @Valid @RequestBody EmployeeDto employeeDto) {
        return ResponseEntity.ok(employeeService.update(id, employeeDto));
    }

    @Operation(summary = "Delete an employee", description = "Only ADMIN can delete an employee")
    @Parameter(name = "id", description = "ID of the employee to delete", required = true)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
