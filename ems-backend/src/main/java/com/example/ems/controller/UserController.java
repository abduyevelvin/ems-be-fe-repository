package com.example.ems.controller;

import com.example.ems.model.LoginResponse;
import com.example.ems.model.SaveUserRequest;
import com.example.ems.model.UpdateUserRequest;
import com.example.ems.model.UserResponse;
import com.example.ems.security.AuthenticatedUser;
import com.example.ems.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid SaveUserRequest request) {
        var userResponse = userService.saveUser(request);

        return ResponseEntity.status(CREATED)
                             .body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid SaveUserRequest request) {

        var loginResponse = userService.login(request);

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
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

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String userId,
                                                   @RequestBody @Valid UpdateUserRequest request) {
        var userResponse = userService.updateUser(userId, request);

        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);

        return ResponseEntity.noContent()
                             .build();
    }
}