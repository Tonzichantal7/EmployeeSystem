package com.example.employeemanagement.service;

import com.example.employeemanagement.entities.Employee;
import com.example.employeemanagement.exception.BadRequestException;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee create(Employee employee) {
        if (employee.getName() == null || employee.getName().isEmpty()) {
            throw new BadRequestException("Employee name cannot be empty");
        }
        if (employee.getPosition() == null || employee.getPosition().isEmpty()) {
            throw new BadRequestException("Employee position cannot be empty");
        }
        return employeeRepository.save(employee);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
    }

    public Employee update(Long id, Employee updated) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));

        if (updated.getName() == null || updated.getName().isEmpty()) {
            throw new BadRequestException("Employee name cannot be empty");
        }
        if (updated.getPosition() == null || updated.getPosition().isEmpty()) {
            throw new BadRequestException("Employee position cannot be empty");
        }

        existing.setName(updated.getName());
        existing.setPosition(updated.getPosition());
        existing.setDepartment(updated.getDepartment());
        existing.setHireDate(updated.getHireDate());

        return employeeRepository.save(existing);
    }

    public void delete(Long id) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
        try {
            employeeRepository.delete(existing);
        } catch (Exception ex) {
            throw new BadRequestException("Failed to delete employee with id " + id + ": " + ex.getMessage());
        }
    }
}
