package com.example.ems.model;

import com.example.ems.enums.GroupType;
import lombok.Builder;

import java.util.Set;

@Builder
public record UserResponse(
        String id,
        String username,
        GroupType group,
        Set<String> permissions
) {
}
