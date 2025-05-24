package com.example.companyservice.service;

import com.example.companyservice.dto.CompanyDto;
import com.example.companyservice.dto.CompanyGetDto;
import com.example.companyservice.dto.CompanyResponseDto;
import com.example.companyservice.dto.UserResponseDto;
import com.example.companyservice.entity.Company;
import com.example.companyservice.exps.RecordAlreadyException;
import com.example.companyservice.exps.RecordNotFoundException;
import com.example.companyservice.repo.CompanyRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final RestTemplate restTemplate;

    public CompanyServiceImpl(CompanyRepository companyRepository, RestTemplate restTemplate) {
        this.companyRepository = companyRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public Company createCompany(CompanyDto companyDto) {
        Optional<Company> existingCompany = companyRepository.findByName(companyDto.getName());
        if (existingCompany.isPresent()) {
            throw new RecordAlreadyException("Company already exists");
        }

        Company company = Company.builder()
                .name(companyDto.getName())
                .budget(companyDto.getBudget())
                .build();

        return companyRepository.save(company);
    }

    @Override
    public CompanyResponseDto getCompanyById(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RecordNotFoundException("Company not found"));
        String url = "http://user-service/api/users/company/" + companyId;
        System.out.println("companyId: " + company);
        ResponseEntity<List<UserResponseDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserResponseDto>>() {
                }
        );
        List<UserResponseDto> users = response.getBody();
        return CompanyResponseDto.builder()
                .id(company.getId())
                .name(company.getName())
                .budget(company.getBudget())
                .users(users)
                .build();
    }

    @Override
    public Company updateCompany(Integer id, CompanyDto companyDto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Company not found"));

        company.setName(companyDto.getName());
        company.setBudget(companyDto.getBudget());

        try {
            return companyRepository.save(company);
        } catch (DataIntegrityViolationException e) {
            throw new RecordAlreadyException("Company with this name already exists");
        }
    }

    @Override
    public void deleteCompanyById(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RecordNotFoundException("Company not found"));

        String url = "http://user-service/api/users/company/" + companyId;
        restTemplate.delete(url);

        companyRepository.delete(company);
    }

    @Override
    public List<CompanyResponseDto> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();

        return companies.stream().map(company -> getCompanyById(company.getId())).collect(Collectors.toList());
    }

    @Override
    public CompanyGetDto getCompanyByIdWithoutUsers(Integer id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Company not found"));

        return CompanyGetDto.builder()
                .id(company.getId())
                .name(company.getName())
                .budget(company.getBudget())
                .build();
    }
}
