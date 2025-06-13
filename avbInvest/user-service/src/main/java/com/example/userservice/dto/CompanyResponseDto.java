package com.example.userservice.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CompanyResponseDto {
    Integer id;
    String name;
    Double budget;
}
