package com.example.companyservice.service;

import com.example.companyservice.client.UserServiceClient;
import com.example.companyservice.dto.CompanyDto;
import com.example.companyservice.dto.CompanyGetDto;
import com.example.companyservice.dto.CompanyResponseDto;
import com.example.companyservice.dto.UserResponseDto;
import com.example.companyservice.entity.Company;
import com.example.companyservice.exps.RecordAlreadyException;
import com.example.companyservice.exps.RecordNotFoundException;
import com.example.companyservice.mapper.CompanyMapper;
import com.example.companyservice.repo.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserServiceClient userServiceClient;

    public CompanyServiceImpl(CompanyRepository companyRepository, UserServiceClient userServiceClient) {
        this.companyRepository = companyRepository;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public CompanyGetDto createCompany(CompanyDto companyDto) {
        checkCompanyNameUniqueness(companyDto.getName());

        Company company = Company.builder()
                .name(companyDto.getName())
                .budget(companyDto.getBudget())
                .build();

        Company saved = companyRepository.save(company);
        log.info("Company saved with id: {}", saved.getId());

        return CompanyMapper.toDto(saved);
    }

    @Override
    public CompanyResponseDto getCompanyById(Integer companyId) {

        Company company = getCompanyOrThrow(companyId);
        List<UserResponseDto> users = userServiceClient.getUsersByCompanyId(companyId);

        log.info("Company: id={}, Users count: {}", company.getId(), users.size());
        return CompanyMapper.toDto(users, company);
    }

    @Override
    public CompanyGetDto updateCompany(Integer id, CompanyDto companyDto) {
        Company company = getCompanyOrThrow(id);
        company.setName(companyDto.getName());
        company.setBudget(companyDto.getBudget());

        try {
            Company updated = companyRepository.save(company);
            log.info("Company updated successfully: {}", updated.getId());
            return CompanyMapper.toDto(updated);
        } catch (DataIntegrityViolationException e) {
            log.error("Update failed. Duplicate name: {}", companyDto.getName(), e);
            throw new RecordAlreadyException("Company with this name already exists");
        }
    }

    @Override
    public void deleteCompanyById(Integer companyId) {
        Company company = getCompanyOrThrow(companyId);
        userServiceClient.deleteUsersByCompanyId(companyId);
        companyRepository.delete(company);

        log.warn("Deleted company with id={}", companyId);
    }

    @Override
    public Page<CompanyResponseDto> getAllCompanies(Pageable pageable) {
        Page<Company> companies = companyRepository.findAll(pageable);

        return companies.map(company -> {
            List<UserResponseDto> users;
            try {
                users = userServiceClient.getUsersByCompanyId(company.getId());
                log.debug("Fetched {} users for company id={}", users.size(), company.getId());
            } catch (Exception e) {
                log.error("Failed to fetch users for company id={}. Error: {}", company.getId(), e.getMessage());
                users = List.of();
            }

            return CompanyMapper.toDto(users, company);
        });
    }

    @Override
    public CompanyGetDto getCompanyByIdWithoutUsers(Integer id) {
        log.info("Fetching company without users with id: {}", id);
        Company company = getCompanyOrThrow(id);
        return CompanyMapper.toDto(company);
    }

    private Company getCompanyOrThrow(Integer id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Company not found with id=" + id));
    }

    private void checkCompanyNameUniqueness(String companyName) {
        if (companyRepository.findByName(companyName).isPresent()) {
            log.warn("Company with name '{}' already exists", companyName);
            throw new RecordAlreadyException("Company already exists");
        }
    }

}
