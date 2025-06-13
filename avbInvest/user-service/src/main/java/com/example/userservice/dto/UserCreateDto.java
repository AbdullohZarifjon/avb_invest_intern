package com.example.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserCreateDto {

    @NotBlank(message = "First name cannot be empty")
    String firstName;

    @NotBlank(message = "Last name cannot be empty")
    String lastName;

    @NotBlank(message = "Phone number cannot be empty")
    String phoneNumber;

    @NotNull(message = "Company ID cannot be empty")
    Integer companyId;
}
