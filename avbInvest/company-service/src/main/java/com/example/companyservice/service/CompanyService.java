package com.example.companyservice.service;

import com.example.companyservice.dto.CompanyDto;
import com.example.companyservice.dto.CompanyGetDto;
import com.example.companyservice.dto.CompanyResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {

    CompanyGetDto createCompany(CompanyDto companyDto);

    CompanyResponseDto getCompanyById(Integer companyId);

    CompanyGetDto updateCompany(Integer id, CompanyDto companyDto);

    void deleteCompanyById(Integer companyId);

    Page<CompanyResponseDto> getAllCompanies(Pageable pageable);

    CompanyGetDto getCompanyByIdWithoutUsers(Integer id);
}
