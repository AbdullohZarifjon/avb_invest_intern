package com.example.userservice.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserResponseDto {
    Integer id;
    String firstName;
    String lastName;
    String phoneNumber;
    Integer companyId;
}