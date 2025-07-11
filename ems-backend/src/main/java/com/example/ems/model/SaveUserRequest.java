package com.example.ems.model;

import jakarta.validation.constraints.NotBlank;

public record SaveUserRequest(
        @NotBlank(message = "Username cannot be blank")
        String username,
        @NotBlank(message = "Password cannot be blank")
        String password
) {
}
