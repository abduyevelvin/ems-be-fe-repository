package com.example.ems.service;

import com.example.ems.model.LoginResponse;
import com.example.ems.model.SaveUserRequest;
import com.example.ems.model.UpdateUserRequest;
import com.example.ems.model.UserResponse;

public interface UserService {

    UserResponse saveUser(SaveUserRequest request);

    LoginResponse login(SaveUserRequest request);

    UserResponse updateUser(String userId, UpdateUserRequest request);

    void deleteUser(String userId);
}
