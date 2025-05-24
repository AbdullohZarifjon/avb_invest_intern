package com.example.userservice.service;

import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.dto.UserGetAllDto;
import com.example.userservice.dto.UserResponseDto;
import com.example.userservice.entity.User;

import java.util.List;

public interface UserService {

    User createUser(UserCreateDto userCreateDto);

    UserGetAllDto getUserById(Integer userId);

    User updateUser(Integer id, UserCreateDto userCreateDto);

    void deleteUserById(Integer userId);

    void deleteUsersByCompanyId(Integer companyId);

    List<UserGetAllDto> getAllUsers();

    List<UserResponseDto> getUserByCompanyId(Integer id);
}
