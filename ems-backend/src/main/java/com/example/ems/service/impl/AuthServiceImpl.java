package com.example.ems.service.impl;

import com.example.ems.entity.PermissionEntity;
import com.example.ems.enums.GroupType;
import com.example.ems.exception.InvalidCredentialsException;
import com.example.ems.exception.ResourceNotFoundException;
import com.example.ems.model.LoginResponse;
import com.example.ems.model.SaveUserRequest;
import com.example.ems.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthServiceImpl {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    public LoginResponse login(SaveUserRequest request) {
        var user = userRepository.findByUserName(request.username())
                                 .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        var group = user.getGroup()
                        .getName();
        var permissions = user.getGroup()
                              .getPermissions()
                              .stream()
                              .map(PermissionEntity::getName)
                              .collect(Collectors.toSet());

        var token = Jwts.builder()
                        .setSubject(user.getUserName())
                        .claim("userId", user.getId())
                        .claim("group", group)
                        .claim("permissions", permissions)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                        .signWith(SignatureAlgorithm.HS256, jwtSecret)
                        .compact();

        return new LoginResponse(user.getUserName(), token, GroupType.valueOf(group), permissions);
    }
}