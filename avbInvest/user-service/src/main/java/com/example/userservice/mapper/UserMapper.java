package com.example.userservice.mapper;

import com.example.userservice.dto.CompanyResponseDto;
import com.example.userservice.dto.UserGetAllComponentsDto;
import com.example.userservice.dto.UserResponseDto;
import com.example.userservice.entity.User;

public class UserMapper {

    public static UserGetAllComponentsDto toDto(User user, CompanyResponseDto companyDto) {
        return UserGetAllComponentsDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .company(companyDto)
                .build();
    }

    public static UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .companyId(user.getCompanyId())
                .build();
    }
}
