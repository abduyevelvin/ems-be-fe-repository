package com.example.ems.service.impl;

import com.example.ems.entity.UserEntity;
import com.example.ems.exception.ResourceAlreadyExistsException;
import com.example.ems.exception.ResourceNotFoundException;
import com.example.ems.model.LoginResponse;
import com.example.ems.model.SaveUserRequest;
import com.example.ems.model.UpdateUserRequest;
import com.example.ems.model.UserResponse;
import com.example.ems.repository.UserGroupRepository;
import com.example.ems.repository.UserRepository;
import com.example.ems.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.ems.mapper.UserMapper.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserGroupRepository userGroupRepository;

    private final AuthServiceImpl authService;

    @Override
    public UserResponse saveUser(SaveUserRequest request) {
        var userEntity = userRepository.findByUserName(request.username());

        if (userEntity.isPresent()) {
            log.warn("User with username {} already exists", request.username());
            throw new ResourceAlreadyExistsException("User with username " + request.username() + " already exists");
        }

        return toUserResponse(userRepository.save(toUserEntity(request)));
    }

    @Override
    public LoginResponse login(SaveUserRequest request) {

        return authService.login(request);
    }

    @Override
    public List<UserResponse> getAllUsers() {

        return toUserResponseList(userRepository.findAll());
    }

    @Override
    public UserResponse updateUser(String userId, UpdateUserRequest request) {
        var existingUser = findUserByIdOrThrow(userId);

        var groupEntity = userGroupRepository.findByName(request.group()
                                                                .name())
                                             .orElseThrow(() -> new ResourceNotFoundException("Group " + request.group()
                                                                                                                .name() +
                                                                                              " not found"));

        existingUser.setGroup(groupEntity);

        return toUserResponse(userRepository.save(existingUser));
    }

    @Override
    public void deleteUser(String userId) {
        findUserByIdOrThrow(userId);

        userRepository.deleteById(userId);
    }

    private UserEntity findUserByIdOrThrow(String userId) {

        return userRepository.findById(userId)
                             .orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " not found"));
    }
}
