package com.example.companyservice.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CompanyGetDto {
    Integer id;
    String name;
    Double budget;
}
