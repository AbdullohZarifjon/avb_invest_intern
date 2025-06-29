package com.example.companyservice.repo;

import com.example.companyservice.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
  Optional<Company> findByName(String name);
}