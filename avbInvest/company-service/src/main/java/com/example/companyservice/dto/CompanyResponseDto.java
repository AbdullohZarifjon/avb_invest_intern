package com.example.companyservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CompanyResponseDto {
    Integer id;
    String name;
    Double budget;
    List<UserResponseDto> users;
}
