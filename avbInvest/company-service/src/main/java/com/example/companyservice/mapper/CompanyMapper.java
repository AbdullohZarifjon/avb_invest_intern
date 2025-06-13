package com.example.companyservice.mapper;

import com.example.companyservice.dto.CompanyGetDto;
import com.example.companyservice.dto.CompanyResponseDto;
import com.example.companyservice.dto.UserResponseDto;
import com.example.companyservice.entity.Company;

import java.util.List;

public class CompanyMapper {

    public static CompanyGetDto toDto(Company company) {
        if (company == null) {
            return null;
        }
        return CompanyGetDto.builder()
                .id(company.getId())
                .name(company.getName())
                .budget(company.getBudget())
                .build();
    }

    public static CompanyResponseDto toDto(List<UserResponseDto> users, Company company) {
        if (company == null) {
            return null;
        }
        if (users == null) {
            users = List.of();
        }

        return CompanyResponseDto.builder()
                .id(company.getId())
                .name(company.getName())
                .budget(company.getBudget())
                .users(users)
                .build();
    }
}
