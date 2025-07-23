package com.example.ems.model;

import com.example.ems.enums.GroupType;

import java.util.Set;

public record LoginResponse(
        String username,
        String token,
        GroupType group,
        Set<String> permissions
) {
}
