package com.example.userservice.controller;

import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.dto.UserGetAllComponentsDto;
import com.example.userservice.dto.UserResponseDto;
import com.example.userservice.entity.SortField;
import com.example.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserGetAllComponentsDto createUser(@Valid @RequestBody UserCreateDto userCreateDto) {
        log.info("Request to create new user: {} {}", userCreateDto.getFirstName(), userCreateDto.getLastName());
        return userService.createUser(userCreateDto);
    }

    @GetMapping("/{id}")
    public UserGetAllComponentsDto getUserById(@PathVariable @Min(1) Integer id) {
        log.info("Fetching user with ID: {}", id);
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserGetAllComponentsDto updateUser(@PathVariable @Min(1) Integer id,
                                              @Valid @RequestBody UserCreateDto userCreateDto) {
        log.info("Updating user with ID: {}, Payload: firstName={}, lastName={}", id,
                userCreateDto.getFirstName(), userCreateDto.getLastName());

        return userService.updateUser(id, userCreateDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable @Min(1) Integer id) {
        log.info("Deleting user with ID: {}", id);
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/company/{id}")
    @Operation(summary = "Delete users by company id")
    public ResponseEntity<Void> deleteUsersByCompanyId(@PathVariable @Min(1) Integer id) {
        log.info("Deleting all users with company ID: {}", id);
        userService.deleteUsersByCompanyId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get paginated list of all users")
    public Page<UserGetAllComponentsDto> getAllUsers(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(defaultValue = "id") SortField sortBy
    ) {
        log.info("GET /api/users called with page={}, size={}, sortBy={}", page, size, sortBy);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy.name()));

        return userService.getAllUsers(pageable);
    }

    @GetMapping("/company/{id}")
    @Operation(summary = "Get Users By Company Id")
    public List<UserResponseDto> getUsersByCompanyId(@PathVariable @Min(1) Integer id) {
        log.info("Fetching users for company ID: {}", id);
        return userService.getUserByCompanyId(id);
    }
}
