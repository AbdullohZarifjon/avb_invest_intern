package com.example.companyservice.client;

import com.example.companyservice.dto.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//@FeignClient(name = "user-service", url = "http://localhost:8082", path = "/api/users")
@FeignClient(name = "user-service", path = "/api/users")
public interface UserServiceClient {

    @GetMapping("/company/{id}")
    @Operation(summary = "Get Users By Company Id")
    List<UserResponseDto> getUsersByCompanyId(@PathVariable("id") Integer id);

    @DeleteMapping("/company/{id}")
    @Operation(summary = "Delete users by company id")
    ResponseEntity<Void> deleteUsersByCompanyId(@PathVariable("id") Integer id);
}

