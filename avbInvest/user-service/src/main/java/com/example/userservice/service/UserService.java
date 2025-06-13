package com.example.userservice.service;

import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.dto.UserGetAllComponentsDto;
import com.example.userservice.dto.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserGetAllComponentsDto createUser(UserCreateDto userCreateDto);

    UserGetAllComponentsDto getUserById(Integer userId);

    UserGetAllComponentsDto updateUser(Integer id, UserCreateDto userCreateDto);

    void deleteUserById(Integer userId);

    void deleteUsersByCompanyId(Integer companyId);

    Page<UserGetAllComponentsDto> getAllUsers(Pageable pageable);

    List<UserResponseDto> getUserByCompanyId(Integer id);
}
