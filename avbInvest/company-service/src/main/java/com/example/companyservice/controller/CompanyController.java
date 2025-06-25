package com.example.companyservice.controller;

import com.example.companyservice.dto.CompanyDto;
import com.example.companyservice.dto.CompanyGetDto;
import com.example.companyservice.dto.CompanyResponseDto;
import com.example.companyservice.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
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
        log.info("Received request to create company: name='{}'", companyDto.getName());
        return companyService.createCompany(companyDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get company with users by ID")
    public CompanyResponseDto getCompanyById(@PathVariable @Min(1) Integer id) {
        log.info("Received request to fetch company with users. ID={}", id);
        return companyService.getCompanyById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing company")
    public CompanyGetDto updateCompany(@PathVariable @Min(1) Integer id,
                                       @Valid @RequestBody CompanyDto companyDto) {
        log.info("Update request for company ID={}", id);
        return companyService.updateCompany(id, companyDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete company by ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable @Min(1) Integer id) {
        log.warn("Delete request received for company ID={}", id);
        companyService.deleteCompanyById(id);
    }

    @GetMapping
    @Operation(summary = "Get paginated list of all companies")
    public Page<CompanyResponseDto> getAllCompanies(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(defaultValue = "id") @Pattern(regexp = "id|name|budget") String sortBy
    ) {
        log.info("Received request to get all companies with pagination: page={}, size={}, sortBy={}",
                page, size, sortBy);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        return companyService.getAllCompanies(pageable);
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "Get company by ID (without users)")
    public CompanyGetDto getCompanyByCompanyId(@PathVariable("id") @Min(1) Integer id) {
        log.info("Received request to fetch company without users. ID={}", id);
        return companyService.getCompanyByIdWithoutUsers(id);
    }
}