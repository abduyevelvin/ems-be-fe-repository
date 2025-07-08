package com.example.ems.mapper;

import com.example.ems.entity.PermissionEntity;
import com.example.ems.entity.UserEntity;
import com.example.ems.entity.UserGroupEntity;
import com.example.ems.enums.GroupType;
import com.example.ems.model.SaveUserRequest;
import com.example.ems.model.UserResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;
import java.util.stream.Collectors;

public class UserMapper {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static UserEntity toEntity(SaveUserRequest request) {
        if (request == null) {
            return null;
        }

        return UserEntity.builder()
                         .id(UUID.randomUUID()
                                 .toString())
                         .userName(request.username())
                         .password(encoder.encode(request.password()))
                         .group(UserGroupEntity.builder()
                                               .id(2)
                                               .build())
                         .build();
    }

    public static UserResponse toResponse(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return UserResponse.builder()
                           .id(entity.getId())
                           .username(entity.getUserName())
                           .group(entity.getGroup() != null ? GroupType.valueOf(entity.getGroup()
                                                                                      .getName()) : null)
                           .permissions(entity.getGroup() != null && entity.getGroup()
                                                                           .getPermissions() != null
                                   ? entity.getGroup()
                                           .getPermissions()
                                           .stream()
                                           .map(PermissionEntity::getName)
                                           .collect(Collectors.toSet())
                                   : java.util.Collections.emptySet())
                           .build();
    }
}