package com.example.ems.service;

import com.example.ems.model.LoginResponse;
import com.example.ems.model.SaveUserRequest;
import com.example.ems.model.UpdateUserRequest;
import com.example.ems.model.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse saveUser(SaveUserRequest request);

    LoginResponse login(SaveUserRequest request);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(String userId, UpdateUserRequest request);

    void deleteUser(String userId);
}
