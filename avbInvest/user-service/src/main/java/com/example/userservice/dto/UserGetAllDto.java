package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserGetAllDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private CompanyResponseDto company;
}