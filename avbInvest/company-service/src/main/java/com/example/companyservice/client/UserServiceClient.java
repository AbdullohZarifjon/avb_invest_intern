package com.example.companyservice.client;

import com.example.companyservice.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-service", path = "/api/users")
public interface UserServiceClient {

    @GetMapping("/company/{id}")
    @Operation(summary = "Get Users By Company Id")
    List<UserResponseDto> getUsersByCompanyId(@PathVariable Integer id);

    @DeleteMapping("/company/{id}")
    @Operation(summary = "Delete users by company id")
    Void deleteUsersByCompanyId(@PathVariable @Min(1) Integer id);
}

