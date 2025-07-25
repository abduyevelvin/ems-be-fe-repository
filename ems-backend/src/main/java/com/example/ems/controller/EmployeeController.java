package com.example.ems.controller;

import com.example.ems.dto.EmployeeDto;
import com.example.ems.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/employees")
@Tag(name = "Employee Management", description = "APIs for managing employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PreAuthorize("hasAuthority('CREATE')")
    @PostMapping
    @Operation(summary = "Create a new employee", description = "Requires CREATE authority")
    public ResponseEntity<EmployeeDto> createEmployee(
            @RequestBody @Valid
            @Parameter(description = "Employee data to create", required = true)
            EmployeeDto employeeDto) {
        var employee = employeeService.createEmployee(employeeDto);
        return ResponseEntity.status(CREATED)
                             .body(employee);
    }

    @PreAuthorize("hasAuthority('LIST')")
    @GetMapping
    @Operation(summary = "Get all employees", description = "Requires LIST authority")
    public ResponseEntity<List<EmployeeDto>> getEmployees() {
        var employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @PreAuthorize("hasAuthority('LIST')")
    @GetMapping("{id}")
    @Operation(summary = "Get employee by ID", description = "Requires LIST authority")
    public ResponseEntity<EmployeeDto> getEmployee(
            @Parameter(description = "Employee ID", required = true)
            @PathVariable("id") Long employeeId) {
        var employee = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.ok(employee);
    }

    @PreAuthorize("hasAuthority('EDIT')")
    @PutMapping("{id}")
    @Operation(summary = "Update employee", description = "Requires EDIT authority")
    public ResponseEntity<EmployeeDto> updateEmployee(
            @Parameter(description = "Employee ID", required = true)
            @PathVariable("id") Long employeeId,
            @RequestBody @Valid
            @Parameter(description = "Updated employee data", required = true)
            EmployeeDto employeeDto) {
        var updatedEmployee = employeeService.updateEmployee(employeeId, employeeDto);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PreAuthorize("hasAuthority('DELETE')")
    @DeleteMapping("{id}")
    @Operation(summary = "Delete employee", description = "Requires DELETE authority")
    public ResponseEntity<Void> deleteEmployee(
            @Parameter(description = "Employee ID", required = true)
            @PathVariable("id") Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent()
                             .build();
    }
}