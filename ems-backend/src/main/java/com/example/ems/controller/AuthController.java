package com.example.ems.controller;

import com.example.ems.model.LoginResponse;
import com.example.ems.model.SaveUserRequest;
import com.example.ems.model.UserResponse;
import com.example.ems.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
@Tag(name = "Authentication", description = "APIs for user registration and login")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<UserResponse> registerUser(
            @RequestBody @Valid
            @Parameter(description = "User registration data", required = true)
            SaveUserRequest request) {
        var userResponse = userService.saveUser(request);
        return ResponseEntity.status(CREATED)
                             .body(userResponse);
    }

    @PostMapping("/login")
    @Operation(summary = "Login user and get JWT token")
    public ResponseEntity<LoginResponse> loginUser(
            @RequestBody @Valid
            @Parameter(description = "User login data", required = true)
            SaveUserRequest request) {
        var loginResponse = userService.login(request);
        return ResponseEntity.ok(loginResponse);
    }
}