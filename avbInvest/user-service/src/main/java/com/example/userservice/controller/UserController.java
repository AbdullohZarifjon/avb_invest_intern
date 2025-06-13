package com.example.userservice.controller;

import com.example.userservice.dto.UserCreateDto;
import com.example.userservice.dto.UserGetAllComponentsDto;
import com.example.userservice.dto.UserResponseDto;
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
        UserGetAllComponentsDto createdUser = userService.createUser(userCreateDto);
        log.info("User created with ID: {}", createdUser.getId());
        return createdUser;
    }

    @GetMapping("/{id}")
    public UserGetAllComponentsDto getUserById(@PathVariable @Min(1) Integer id) {
        log.info("Fetching user with ID: {}", id);
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserGetAllComponentsDto updateUser(@PathVariable @Min(1) Integer id,
                                              @Valid @RequestBody UserCreateDto userCreateDto) {
        log.info("Updating user with ID: {}, Payload: {}", id, userCreateDto.getPhoneNumber());
        UserGetAllComponentsDto updatedUser = userService.updateUser(id, userCreateDto);
        log.info("User with ID: {} updated successfully", id);
        return updatedUser;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable @Min(1) Integer id) {
        log.info("Deleting user with ID: {}", id);
        userService.deleteUserById(id);
        log.info("User with ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/company/{id}")
    @Operation(summary = "Delete users by company id")
    public ResponseEntity<Void> deleteUsersByCompanyId(@PathVariable @Min(1) Integer id) {
        log.info("Deleting all users with company ID: {}", id);
        userService.deleteUsersByCompanyId(id);
        log.info("All users from company ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Get paginated list of all users")
    public Page<UserGetAllComponentsDto> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        log.info("GET /api/users called with page={}, size={}, sortBy={}", page, size, sortBy);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
            return userService.getAllUsers(pageable);
    }

    @GetMapping("/company/{id}")
    @Operation(summary = "Get Users By Company Id")
    public List<UserResponseDto> getUsersByCompanyId(@PathVariable @Min(1) Integer id) {
        log.info("Fetching users for company ID: {}", id);
        List<UserResponseDto> users = userService.getUserByCompanyId(id);
        log.debug("Fetched {} users for company ID: {}", users.size(), id);
        return users;
    }
}
