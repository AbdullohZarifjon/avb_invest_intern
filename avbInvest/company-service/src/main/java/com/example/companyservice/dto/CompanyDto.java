package com.example.companyservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyDto {
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Budget cannot be null")
    private Double budget;

}
