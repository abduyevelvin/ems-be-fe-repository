package com.example.ems.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record EmployeeDto(
        Long employeeId,
        @NotNull(message = "Employee First Name cannot be null")
        String firstName,
        @NotNull(message = "Employee Last Name cannot be null")
        String lastName,
        @NotNull(message = "Employee Email cannot be null")
        @Email(message = "Employee Email should be valid")
        String email
) {
}
