package com.example.employeemanagement.controller;

import com.example.employeemanagement.entities.Employee;
import com.example.employeemanagement.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
    @RequestBody(description = "Employee details to create", required = true)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Employee> create(@Valid @RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.create(employee));
    }

    @Operation(summary = "Get all employees", description = "USER and ADMIN can view all employees")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    @GetMapping
    public ResponseEntity<List<Employee>> all() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @Operation(summary = "Get an employee by ID", description = "USER and ADMIN can view a single employee")
    @Parameter(name = "id", description = "ID of the employee to retrieve", required = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<Employee> one(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @Operation(summary = "Update an employee", description = "Only ADMIN can update employee details")
    @Parameter(name = "id", description = "ID of the employee to update", required = true)
    @RequestBody(description = "Updated employee details", required = true)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable Long id, @Valid @RequestBody Employee update) {
        return ResponseEntity.ok(employeeService.update(id, update));
    }

    @Operation(summary = "Delete an employee", description = "Only ADMIN can delete an employee")
    @Parameter(name = "id", description = "ID of the employee to delete", required = true)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
