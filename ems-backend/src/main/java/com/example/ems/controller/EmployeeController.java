package com.example.ems.controller;

import com.example.ems.dto.EmployeeDto;
import com.example.ems.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    // This endpoint allows you to create a new employee in the DB with the provided details.
    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody @Valid EmployeeDto employeeDto) {
        var employee = employeeService.createEmployee(employeeDto);

        return ResponseEntity.status(CREATED)
                             .body(employee);
    }

    // This endpoint retrieves an employee by its id from the DB.
    @GetMapping("{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("id") Long employeeId) {
        var employee = employeeService.getEmployeeById(employeeId);

        return ResponseEntity.ok(employee);
    }

    // This endpoint retrieves all employees from the DB.
    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getEmployees() {
        var employees = employeeService.getAllEmployees();

        return ResponseEntity.ok(employees);
    }

    // This endpoint updates an existing employee in the DB with the provided details.
    @PutMapping("{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable("id") Long employeeId,
                                                      @RequestBody @Valid EmployeeDto employeeDto) {
        var updatedEmployee = employeeService.updateEmployee(employeeId, employeeDto);

        return ResponseEntity.ok(updatedEmployee);
    }

    // This endpoint deletes an employee by its id from the DB.
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long employeeId) {
        employeeService.deleteEmployee(employeeId);

        return ResponseEntity.noContent()
                             .build();
    }
}
