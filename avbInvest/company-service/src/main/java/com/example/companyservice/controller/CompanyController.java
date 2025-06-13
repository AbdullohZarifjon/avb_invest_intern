package com.example.companyservice.controller;

import com.example.companyservice.dto.CompanyDto;
import com.example.companyservice.dto.CompanyGetDto;
import com.example.companyservice.dto.CompanyResponseDto;
import com.example.companyservice.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    @Operation(summary = "Create a new company")
    public CompanyGetDto addCompany(@Valid @RequestBody CompanyDto companyDto) {
        log.info("Received request to create company: name='{}', budget={}",
                companyDto.getName(), companyDto.getBudget());
        CompanyGetDto createdCompany = companyService.createCompany(companyDto);
        log.info("Company created successfully with id={}", createdCompany.getId());
        return createdCompany;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get company with users by ID")
    public CompanyResponseDto getCompanyById(@PathVariable Integer id) {
        log.info("Received request to fetch company with users. ID={}", id);
        CompanyResponseDto response = companyService.getCompanyById(id);
        log.info("Successfully fetched company '{}'", response.getName());
        return response;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing company")
    public CompanyGetDto updateCompany(@PathVariable Integer id,
                                       @Valid @RequestBody CompanyDto companyDto) {
        log.info("Received request to update company with id={}", id);
        CompanyGetDto updatedCompany = companyService.updateCompany(id, companyDto);
        log.info("Company updated successfully: id={}, name={}", updatedCompany.getId(), updatedCompany.getName());
        return updatedCompany;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete company by ID")
    public ResponseEntity<Void> deleteCompany(@PathVariable Integer id) {
        log.info("Received request to delete company with id={}", id);
        companyService.deleteCompanyById(id);
        log.info("Company deleted successfully with id={}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get paginated list of all companies")
    public Page<CompanyResponseDto> getAllCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        log.info("Received request to get all companies with pagination: page={}, size={}, sortBy={}",
                page, size, sortBy);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<CompanyResponseDto> companies = companyService.getAllCompanies(pageable);

        log.info("Returning {} companies", companies.getTotalElements());
        return companies;
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "Get company by ID (without users)")
    public CompanyGetDto getCompanyByCompanyId(@PathVariable Integer id) {
        log.info("Received request to fetch company without users. ID={}", id);
        CompanyGetDto company = companyService.getCompanyByIdWithoutUsers(id);
        log.info("Successfully fetched company '{}'", company.getName());
        return company;
    }
}