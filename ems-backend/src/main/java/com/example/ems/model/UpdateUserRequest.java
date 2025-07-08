package com.example.ems.model;

import com.example.ems.enums.GroupType;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRequest(
        @NotNull(message = "Group cannot be null")
        GroupType group
) {
}
