package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployeeDto;
import com.example.employeemanagement.entities.Employee;
import com.example.employeemanagement.entities.Role;
import com.example.employeemanagement.entities.User;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;


    private final UserRepository userRepository;

    public void promoteEmployeeToAdmin(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        User admin = new User();
        admin.setUsername(employee.getName().toLowerCase().replace(" ", "") + employee.getId());
        admin.setEmail(employee.getName() + employee.getId() + "@company.com");
        admin.setPassword(passwordEncoder.encode("Admin123"));
        admin.setRole(Role.ROLE_ADMIN);

        admin.setEmployeesManaged(new ArrayList<>());
        admin.getEmployeesManaged().add(employee);

        userRepository.save(admin);
    }

    public EmployeeDto create(EmployeeDto dto) {
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setPosition(dto.getPosition());
        employee.setDepartment(dto.getDepartment());
        employee.setHireDate(dto.getHireDate());

        Employee saved = employeeRepository.save(employee);
        return toDto(saved);
    }

    public List<EmployeeDto> findAll() {
        return employeeRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public EmployeeDto findById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return toDto(employee);
    }

    public EmployeeDto update(Long id, EmployeeDto dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setName(dto.getName());
        employee.setPosition(dto.getPosition());
        employee.setDepartment(dto.getDepartment());
        employee.setHireDate(dto.getHireDate());

        Employee updated = employeeRepository.save(employee);
        return toDto(updated);
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    private EmployeeDto toDto(Employee employee) {
        return new EmployeeDto(employee.getId(),
                employee.getName(),
                employee.getPosition(),
                employee.getDepartment(),
                employee.getHireDate());
    }
}
