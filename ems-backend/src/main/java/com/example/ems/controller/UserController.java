package com.example.ems.controller;

import com.example.ems.model.UpdateUserRequest;
import com.example.ems.model.UserResponse;
import com.example.ems.security.AuthenticatedUser;
import com.example.ems.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
@Tag(name = "User Management", description = "APIs for managing users and authentication")
public class UserController {

    private final UserService userService;

//    @PostMapping("/register")
//    @Operation(summary = "Register a new user")
//    public ResponseEntity<UserResponse> registerUser(
//            @RequestBody @Valid
//            @Parameter(description = "User registration data", required = true)
//            SaveUserRequest request) {
//        var userResponse = userService.saveUser(request);
//        return ResponseEntity.status(CREATED)
//                             .body(userResponse);
//    }
//
//    @PostMapping("/login")
//    @Operation(summary = "Login user and get JWT token")
//    public ResponseEntity<LoginResponse> loginUser(
//            @RequestBody @Valid
//            @Parameter(description = "User login data", required = true)
//            SaveUserRequest request) {
//        var loginResponse = userService.login(request);
//        return ResponseEntity.ok(loginResponse);
//    }

    @GetMapping
    @Operation(summary = "Get all users or current user", description = "Returns all users for admin, current user otherwise")
    public ResponseEntity<List<UserResponse>> getAllUsers(
            @AuthenticationPrincipal
            @Parameter(description = "Authenticated user", required = true)
            AuthenticatedUser authenticatedUser) {
        boolean isAdmin = authenticatedUser.getAuthorities()
                                           .stream()
                                           .anyMatch(a -> a.getAuthority()
                                                           .equals("ROLE_ADMIN"));
        if (isAdmin) {
            return ResponseEntity.ok(userService.getAllUsers());
        } else {
            var currentUser = userService.getAllUsers()
                                         .stream()
                                         .filter(u -> u.username()
                                                       .equals(authenticatedUser.getUsername()))
                                         .toList();
            return ResponseEntity.ok(currentUser);
        }
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID", description = "Returns user by ID if admin or self")
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId,
            @AuthenticationPrincipal
            @Parameter(description = "Authenticated user", required = true)
            AuthenticatedUser authenticatedUser) {
        boolean isAdmin = authenticatedUser.getAuthorities()
                                           .stream()
                                           .anyMatch(a -> a.getAuthority()
                                                           .equals("ROLE_ADMIN"));
        if (!isAdmin && !authenticatedUser.getUserId()
                                          .equals(userId)) {
            return ResponseEntity.status(FORBIDDEN)
                                 .build();
        }
        var userResponse = userService.getUserById(userId);
        return ResponseEntity.ok(userResponse);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}")
    @Operation(summary = "Update user", description = "Requires ADMIN role")
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId,
            @RequestBody @Valid
            @Parameter(description = "Updated user data", required = true)
            UpdateUserRequest request) {
        var userResponse = userService.updateUser(userId, request);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user", description = "Requires ADMIN role")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent()
                             .build();
    }
}