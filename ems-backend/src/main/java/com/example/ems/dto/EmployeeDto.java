package com.example.ems.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmployeeDto(
        Long employeeId,
        @NotBlank(message = "Employee First Name cannot be null or empty")
        String firstName,
        @NotBlank(message = "Employee Last Name cannot be null or empty")
        String lastName,
        @NotBlank(message = "Employee Email cannot be null or empty")
        @Email(message = "Employee Email should be valid")
        String email
) {
}
