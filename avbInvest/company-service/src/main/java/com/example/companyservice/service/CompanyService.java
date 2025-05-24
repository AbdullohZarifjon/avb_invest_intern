package com.example.companyservice.service;

import com.example.companyservice.dto.CompanyDto;
import com.example.companyservice.dto.CompanyGetDto;
import com.example.companyservice.dto.CompanyResponseDto;
import com.example.companyservice.entity.Company;

import java.util.List;

public interface CompanyService {

    Company createCompany(CompanyDto companyDto);

    CompanyResponseDto getCompanyById(Integer companyId);

    Company updateCompany(Integer id, CompanyDto companyDto);

    void deleteCompanyById(Integer companyId);

    List<CompanyResponseDto> getAllCompanies();

    CompanyGetDto getCompanyByIdWithoutUsers(Integer id);
}
